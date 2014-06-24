/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    brightness.rs
 * This is the renderscript that adds the threshold blur to images.
 */

#pragma version(1)
#pragma rs java_package_name(zwaggerboyz.instaswaggify)
#pragma rs_fp_relaxed

rs_allocation input;
int imageWidth;
int imageHeight;

int radius;

float drop;
int threshold;
float strength;

uchar4 __attribute__((kernel)) threshold_blur(uint32_t x, uint32_t y) {
    uchar4 in = rsGetElementAt_uchar4(input, x, y);
    float4 diff4;
    int new_y, new_x;
    float4 neighbour;
    float4 out;
    int x_offset, y_offset;
    float diff, dist;

    drop = 1.5;

    /* Using the colors (un)packer functions has a different effect.
     */
    out = convert_float4(in);

    /* Here a loop is needed that iteratates from -radius to radius. But renderscript doesn't support
     * negative values in a loop, hence the 2*radius.
     */
    for (x_offset = 0; x_offset <= 2*radius; x_offset++) {
        new_x = x + x_offset - radius;
        x_offset = abs(x_offset);

        if (new_x < 0 || new_x >= imageWidth)
            continue;

        for (y_offset = 0; y_offset <= 2*radius - x_offset; y_offset++) {
            int condition = 0;

            new_y = y + y_offset - radius;
            y_offset = abs(y_offset);

            if (   new_y >= 0
                && new_y < imageHeight
                && x_offset + y_offset <= 2 * radius
                ) {

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

    return convert_uchar4(out);
}

