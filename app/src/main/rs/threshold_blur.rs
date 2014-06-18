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

uchar4 __attribute__((kernel)) threshold_blur(uchar4 in, uint32_t x, uint32_t y) {
    int redDif, greenDif, blueDif;
    uchar red, green, blue, neighbor;
    long new_y, new_x;
    uchar4 out, neighbour;
    int diff, dist, done = 0;

    uchar4 empty = {255, 0, 0, 0};

    radius = 100;
    drop = 5;
    threshold = 5;
    strength = 2;

    out = in;
    rsDebug("root", "hello");
    return in;

    //rsDebug("root imageWidth", imageWidth);
    //rsDebug("root imageHeight", imageHeight);

    for (int x_offset = -radius; x_offset <= radius; x_offset++) {
        new_x = x + x_offset;

        for (int y_offset = -radius; y_offset <= radius; y_offset++) {
            new_y = y + y_offset;
            if (new_y >= 0
                && new_y < imageHeight
                && new_x != x
                && new_y != y
                && new_x >= 0
                && new_x < imageWidth) {

                rsDebug("new_y", (uint32_t)new_y);
                rsDebug("new_x", (uint32_t)new_x);

                done = 1;
                neighbour = rsGetElementAt_uchar4(input, (uint32_t)new_x, (uint32_t)new_y);

                if (x_offset < 0)
                    x_offset = x_offset * -1;
                if (y_offset < 0)
                    y_offset = y_offset * -1;

                if(x_offset > y_offset)
                    dist = (int)x_offset;
                else
                    dist = (int)y_offset;

                dist = dist * drop;
                redDif = (int)abs(out.r - neighbour.r);
                blueDif = (int)abs(out.b - neighbour.b);
                greenDif = (int)abs(out.g - neighbour.g);

                diff = redDif * redDif + greenDif * greenDif + blueDif * blueDif;
                if (diff > threshold) {
                    continue;
                }

                out.r = (out.r * (uchar)dist + neighbour.r * strength) / ((uchar)dist + strength);
                out.g = (out.g * (uchar)dist + neighbour.g * strength) / ((uchar)dist + strength);
                out.b = (out.b * (uchar)dist + neighbour.b * strength) / ((uchar)dist + strength);
            }
        }

    }

    if (done)
        return out;
    else
        return empty;
}

