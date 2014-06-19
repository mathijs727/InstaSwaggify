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

const static float3 gMonoMult = {0.299f, 0.587f, 0.114f};

float depth;
float intensity;
float3 gSepiaVector;

void calculateVector() {
    gSepiaVector.r = depth * 2;
    gSepiaVector.g = depth;
    gSepiaVector.b = -intensity;
}

/*
RenderScript kernel that performs sepia manipulation.
*/
uchar4 __attribute__((kernel)) sepia(uchar4 in)
{
    float4 f4 = rsUnpackColor8888(in);
    float3 f3 = dot(f4.rgb, gMonoMult);

    f3 += gSepiaVector;

    /* clipping check */
    float3 result = clamp(f3, 0.f, 1.f);

    return rsPackColorTo8888(result);
}