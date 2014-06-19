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
int imageWidth;
int imageHeight;

int radius;

int drop;
int threshold;
uchar strength;

/*
 RenderScript kernel that inverets the colors.
 */

uchar4 __attribute__((kernel)) threshold_blur(uint32_t x, uint32_t y) {
    uchar4 in = rsGetElementAt_uchar4(input, x, y);
    float4 diff4;
    int new_y, new_x;
    float4 neighbour;
    float4 out;
    int done = 0;
    float diff, dist;

    uchar4 empty = {255, 0, 0, 255};

    radius = 10;
    drop = 2;
    threshold = 500;
    strength = 2;

    out = convert_float4(in);


    for (int x_offset = -radius; x_offset <= radius; x_offset++) {
        new_x = x + x_offset;

        for (int y_offset = -radius; y_offset <= radius; y_offset++) {
            new_y = y + y_offset;

            rsDebug("---------------", 0);
            rsDebug("new_y >= 0 ||", new_y >= 0);
            rsDebug("new_x >= 0 ||", new_x >= 0);
            rsDebug("new_x < imagewidth ||", x < imageWidth);
            rsDebug("new_y < imageHeight ||", y < imageHeight);
            rsDebug("x_offset + y_offset <= radius ||", x_offset + y_offset <= radius);

            x_offset = abs(x_offset);
            y_offset = abs(y_offset);
            if (   new_y >= 0
                && new_x >= 0
                && new_x < imageWidth
                && new_y < imageHeight
                && x_offset + y_offset <= radius) {

                done = 1;
                neighbour = convert_float4(rsGetElementAt_uchar4(input, (uint32_t)new_x, (uint32_t)new_y));

                dist = max(x_offset, y_offset);

                dist = dist * drop;

                diff4 = fabs(out - neighbour);
                diff = dot(diff4, diff4);

                if (diff > threshold) {
                    continue;
                }

                out = (out * dist + neighbour * strength) / (dist + strength);
            }
        }

    }

    if (done) {
        rsDebug("is done?", 1);
        return convert_uchar4(out);
    }
    else {
        rsDebug("is done?", 0);
        return empty;
    }
}

