#version 330
#extension GL_ARB_separate_shader_objects : require
uniform sampler2D InSampler;
layout(location=0) in vec2 texCoord;
layout(std140) uniform WarpColorConfig { vec3 ColorScale; float Saturation; };
layout(location=0) out vec4 fragColor;
const vec3 Gray=vec3(0.3,0.59,0.11);
void main(){ vec4 c=texture(InSampler,texCoord); vec3 s=c.rgb*ColorScale; float l=dot(s,Gray); fragColor=vec4((s-l)*Saturation+l,c.a); }
