/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    brightness.rs
 * This is the renderscript that adds noise to images.
 */

#pragma version(1)
#pragma rs java_package_name(zwaggerboyz.instaswaggify)
#pragma rs_fp_relaxed

float noiseValue = 0.f;

uchar4 __attribute__((kernel)) noise(uchar4 in, uint32_t x, uint32_t y) {
    float4 f4 = rsUnpackColor8888(in);
    float3 f3 = {   noiseValue * 2 * (rsRand(1.f) - 0.5f) ,
                    noiseValue * 2 * (rsRand(1.f) - 0.5f) ,
                    noiseValue * 2 * (rsRand(1.f) - 0.5f)};
    float3 result = f4.rgb + f3;
    result = clamp(result, 0.f, 1.f);
    return rsPackColorTo8888(result);

}
