#version 330 core

#moj_import <nw-cheat:mesh_data.glsl>

layout(location = 0) in vec3 pos;
layout(location = 1) in vec4 color;

out vec4 v_Color;

void main() {
    gl_Position = u_Proj * u_ModelView * vec4(pos, 1.);

    v_Color = color;
}