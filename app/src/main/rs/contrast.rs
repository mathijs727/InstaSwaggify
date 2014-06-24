/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    contrast.rs
 * This is the renderscript that adjusts the contrast of images.
 */

#pragma version(1)
#pragma rs java_package_name(zwaggerboyz.instaswaggify)
#pragma rs_fp_relaxed

const static float3 gHalf = {.5f, .5f, .5f};

float contrastValue = 0.f;

uchar4 __attribute__((kernel)) contrast(uchar4 in)
{
    float4 f4 = rsUnpackColor8888(in);
    float3 result = ((f4.rgb - gHalf) * contrastValue) + gHalf;
    result = clamp(result, 0.f, 1.f);
    return rsPackColorTo8888(result);
}