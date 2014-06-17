/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#pragma version(1)
#pragma rs java_package_name(zwaggerboyz.instaswaggify)
#pragma rs_fp_relaxed

rs_allocation input;
float rotationAngle;
int imageWidth;
int imageHeight = 0;
rs_matrix4x4 matrix;

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

    if (trans_x > 0 && trans_x < imageWidth && trans_y > 0 && trans_y < imageHeight) {
        return rsGetElementAt_uchar4(input, trans_x, trans_y);
    } else {
        return rsPackColorTo8888(1.0f, 1.0f, 1.0f);
    }
}