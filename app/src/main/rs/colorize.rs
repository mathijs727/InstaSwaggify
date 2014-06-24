/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    colorize.rs
 * This is the renderscript that adjusts the color-intensitiy of images.
 */

#pragma version(1)
#pragma rs java_package_name(zwaggerboyz.instaswaggify)
#pragma rs_fp_relaxed

float blueValue;
float greenValue;
float redValue;

uchar4 __attribute__((kernel)) colorize(uchar4 in, uint32_t x, uint32_t y) {
    float4 f4 = rsUnpackColor8888(in);
    float3 f3 = {redValue, greenValue, blueValue};
    float3 result =  f3 + f4.rgb;
    result = clamp(result, 0.f, 1.f);
    return rsPackColorTo8888(result);
}