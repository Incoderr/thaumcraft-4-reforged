import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import thaumcraft.client.geom.ClippedQuadSink;
import thaumcraft.client.geom.ProjectedWindow;
import thaumcraft.client.geom.QuadSink;

/** Headless checks for scanner clipping; no game or graphics context required. */
public final class ClientGeometryTest {
    private static final float[][] HEX = {
        {-0.22F, 0.38F}, {0.22F, 0.38F}, {0.44F, 0},
        {0.22F, -0.38F}, {-0.22F, -0.38F}, {-0.44F, 0}
    };
    private record Vertex(float x, float y, float z, float u, float v) {}
    private static final class Output implements QuadSink {
        final List<Vertex> vertices = new ArrayList<>();
        public void vertex(float x, float y, float z, float u, float v, int color, int light) {
            vertices.add(new Vertex(x, y, z, u, v));
            check(color == 0xFFABCDEF && light == 220, "color and light must survive clipping");
        }
    }
    private static void check(boolean pass, String reason) {
        if (!pass) throw new AssertionError(reason);
    }
    private static void quad(QuadSink sink, float left, float right, float bottom, float top, float z) {
        sink.vertex(left, bottom, z, 0, 0, 0xFFABCDEF, 220);
        sink.vertex(right, bottom, z, 1, 0, 0xFFABCDEF, 220);
        sink.vertex(right, top, z, 1, 1, 0xFFABCDEF, 220);
        sink.vertex(left, top, z, 0, 1, 0xFFABCDEF, 220);
    }
    private static float distance(Vector4f plane, float x, float y, float z) {
        return plane.x * x + plane.y * y + plane.z * z + plane.w;
    }
    private static boolean inside(List<Vector4f> planes, Vector4f point) {
        return planes.stream().allMatch(p -> p.dot(point) >= -1.0E-5F);
    }
    private static Vector4f scenePoint(Matrix4f scene, Matrix4f glass, float x, float y) {
        Vector4f clip = glass.transform(new Vector4f(x, y, 0, 1));
        clip.div(clip.w);
        clip.z = 0.5F;
        Vector4f result = scene.invert(new Matrix4f()).transform(clip);
        return result.div(result.w);
    }
    public static void main(String[] args) {
        check(!thaumcraft.client.geom.EldritchSurfaceGeometry.exposed(true, false), "adjacent void surfaces hide their shared face");
        check(!thaumcraft.client.geom.EldritchSurfaceGeometry.exposed(false, true), "solid neighbors hide void faces");
        check(thaumcraft.client.geom.EldritchSurfaceGeometry.exposed(false, false), "void surface borders air");
        Output surface = new Output();
        thaumcraft.client.geom.EldritchSurfaceGeometry.faces(surface, 0b100001, 1, 0, 0xFFABCDEF, 220);
        check(surface.vertices.size() == 8, "only two exposed faces emit vertices");
        Output full = new Output();
        quad(new ClippedQuadSink(full, List.of()), -1, 1, -1, 1, 0);
        check(full.vertices.size() == 4, "an uncut quad remains intact");

        Output half = new Output();
        quad(new ClippedQuadSink(half, List.of(new Vector4f(1, 0, 0, 0))), -1, 1, -1, 1, 0);
        check(half.vertices.size() == 4, "half quad must have four corners");
        for (Vertex v : half.vertices) {
            check(v.x >= 0 && Math.abs(v.u - (v.x + 1) / 2) < 1.0E-6, "edge UVs must interpolate");
        }

        Output hidden = new Output();
        quad(new ClippedQuadSink(hidden, ProjectedWindow.hidden()), -1, 1, -1, 1, 0);
        check(hidden.vertices.isEmpty(), "hidden glass reveals no geometry");

        for (float fov : new float[] {30, 70, 110}) {
            for (float aspect : new float[] {1, 16.0F / 9, 21.0F / 9}) {
                Matrix4f scene = new Matrix4f().perspective((float) Math.toRadians(fov), aspect, 0.05F, 128);
                Matrix4f glass = new Matrix4f().perspective((float) Math.toRadians(70), aspect, 0.05F, 128)
                    .translate(0.2F, -0.15F, -1).rotateZ(0.2F).rotateY(0.15F);
                List<Vector4f> planes = ProjectedWindow.planes(HEX, glass, scene);
                check(inside(planes, scenePoint(scene, glass, 0, 0)), "glass center visible at every FOV/aspect");
                check(!inside(planes, scenePoint(scene, glass, 0.48F, 0.35F)), "hex corners must exclude rectangle corners");
                check(!inside(planes, scenePoint(scene, glass, 0.8F, 0)), "outside glass must stay invisible");
                check(!inside(planes, new Vector4f(0, 0, 2, 1)), "nodes behind camera must stay invisible");
                Output cut = new Output();
                quad(new ClippedQuadSink(cut, planes), -8, 8, -8, 8, -8);
                check(!cut.vertices.isEmpty() && cut.vertices.size() % 4 == 0, "large node clips to valid quads");
                for (Vertex v : cut.vertices) {
                    for (Vector4f plane : planes) check(distance(plane, v.x, v.y, v.z) >= -1.0E-4, "no vertex spills outside glass");
                    check(Math.abs(v.u - (v.x + 8) / 16) < 1.0E-5, "clipped polygons preserve horizontal UVs");
                    check(Math.abs(v.v - (v.y + 8) / 16) < 1.0E-5, "clipped polygons preserve vertical UVs");
                }
                Vector4f formerCenter = scenePoint(scene, glass, 0, 0);
                Matrix4f lowered = new Matrix4f().perspective((float) Math.toRadians(70), aspect, 0.05F, 128).translate(0, -1.5F, -1);
                check(!inside(ProjectedWindow.planes(HEX, lowered, scene), formerCenter), "aperture follows equip movement");
            }
        }
        Matrix4f projection = new Matrix4f().perspective(1.2F, 1.6F, 0.05F, 128);
        List<Vector4f> backwards = ProjectedWindow.planes(HEX, new Matrix4f(projection).translate(0, 0, 1), projection);
        Output invalid = new Output();
        quad(new ClippedQuadSink(invalid, backwards), -1, 1, -1, 1, -2);
        check(invalid.vertices.isEmpty(), "glass behind camera must disable revealing");
        System.out.println("Client geometry checks passed (UV clipping, hex edges, FOV/aspect, equip movement, rear camera).");
    }
}
