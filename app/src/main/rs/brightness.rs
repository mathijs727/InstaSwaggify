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

float brightnessValue = 0.f;

/*
RenderScript kernel that performs brightness manipulation.
*/
uchar4 __attribute__((kernel)) brightness(uchar4 in)
{
    float4 f4 = rsUnpackColor8888(in);
    float3 result = f4.rgb * brightnessValue;

    return rsPackColorTo8888(result);
}