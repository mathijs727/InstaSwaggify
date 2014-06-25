/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    rotation.rs
 * This is the renderscript that adjusts the rotation of images.
 */

#pragma version(1)
#pragma rs java_package_name(zwaggerboyz.instaswaggify)
#pragma rs_fp_relaxed

rs_allocation input;
float rotationAngle;
int imageWidth;
int imageHeight;
rs_matrix4x4 matrix;


void init() {
    rsMatrixLoadIdentity(&matrix);
}

void calculateMatrix() {
    rsMatrixLoadIdentity(&matrix);
    rsMatrixTranslate(&matrix, imageWidth/2.0f, imageHeight/2.0f, 0.0f);
    rsMatrixRotate(&matrix, rotationAngle, 0.0f, 0.0f, 1.0f);
    rsMatrixTranslate(&matrix, -imageWidth/2.0f, -imageHeight/2.0f, 0.0f);
}

/*
RenderScript kernel that performs rotation.
*/
uchar4 __attribute__((kernel)) rotation(uint32_t x, uint32_t y)
{
    float4 in_vec = {x, y, 0.0f, 1.0f};
    float4 trans = rsMatrixMultiply(&matrix, in_vec);

    float trans_x = trans.x;
    float trans_y = trans.y;

    if (trans_x >= 0 && trans_x <= imageWidth && trans_y >= 0 && trans_y <= imageHeight) {
        return rsGetElementAt_uchar4(input, trans_x, trans_y);
    } else {
        return rsPackColorTo8888(1.0f, 1.0f, 1.0f);
    }
}