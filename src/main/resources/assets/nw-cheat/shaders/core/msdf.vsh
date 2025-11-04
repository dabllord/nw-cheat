#version 330 core

#moj_import <nw-cheat:mesh_data.glsl>

layout(location = 0) in vec3 pos;
layout(location = 1) in vec2 uv;
layout(location = 2) in vec4 color;

out vec2 v_UV;
out vec4 v_Color;

void main() {
    gl_Position = u_Proj * u_ModelView * vec4(pos, 1.0);
    v_UV = uv;
    v_Color = color;
}