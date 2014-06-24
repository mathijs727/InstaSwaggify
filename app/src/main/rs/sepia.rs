/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    brightness.rs
 * This is the renderscript that adds sepia colors to images.
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

uchar4 __attribute__((kernel)) sepia(uchar4 in)
{
    float4 f4 = rsUnpackColor8888(in);
    float3 f3 = dot(f4.rgb, gMonoMult);

    f3 += gSepiaVector;

    /* clipping check */
    float3 result = clamp(f3, 0.f, 1.f);

    return rsPackColorTo8888(result);
}