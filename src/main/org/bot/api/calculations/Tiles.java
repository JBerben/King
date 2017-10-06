package src.main.org.bot.api.calculations;

import java.awt.*;

public class Tiles {
/*
    private int method42(int i, int j, int k)
    {
        int l = k >> 7;
        int i1 = j >> 7;
        if ((l < 0) || (i1 < 0) || (l > 103) || (i1 > 103)) {
            return 0;
        }
        int j1 = i;
        if ((j1 < 3) && ((this.byteGroundArray[1][l][i1] & 0x2) == 2)) {
            j1++;
        }
        int k1 = k & 0x7F;
        int l1 = j & 0x7F;
        int i2 = this.intGroundArray[j1][l][i1] * (128 - k1) + this.intGroundArray[j1][(l + 1)][i1] * k1 >> 7;

        int j2 = this.intGroundArray[j1][l][(i1 + 1)] * (128 - k1) + this.intGroundArray[j1][(l + 1)][(i1 + 1)] * k1 >> 7;

        return i2 * (128 - l1) + j2 * l1 >> 7;
    }

    private void calcEntityScreenPos(int i, int j, int l)
    {
        if ((i < 128) || (l < 128) || (i > 13056) || (l > 13056)) {
            return;
        }
        int i1 = method42(his.planet, l, i) - j;
        i -= Client.getCameraX();
        i1 -= Client.getCameraY();
        l -= Client.getCameraZ()
        int j1 = Model.modelIntArray1[this.yCameraCurve];
        int k1 = Model.modelIntArray2[this.yCameraCurve];
        int l1 = Model.modelIntArray1[this.xCameraCurve];
        int i2 = Model.modelIntArray2[this.xCameraCurve];
        int j2 = l * l1 + i * i2 >> 16;
        l = l * i2 - i * l1 >> 16;
        i = j2;
        j2 = i1 * k1 - l * j1 >> 16;
        l = i1 * j1 + l * k1 >> 16;
        i1 = j2;
        if (l >= 50)
        {
            this.spriteDrawX = (Texture.textureInt1 + (i << 9) / l);
            this.spriteDrawY = (Texture.textureInt2 + (i1 << 9) / l);
        }
        else
        {
            this.spriteDrawX = -1;
            this.spriteDrawY = -1;
        }
    }

    public static Point tileToCanvas(int x, int y, int plane, int offset) {
        if (x >= 128 && y >= 128 && x <= 13056 && y <= 13056) {
            int z = getTileHeight(x, y, Client.getPlane()) - plane;
            x -= Client.getCameraX();
            y -= Client.getCameraY();
            z -= Client.getCameraZ();
            z -= offset;

            int pitch = (int) Client.getPitch();
            int yaw = Client.getYaw();

            int pitchSin = (int) Math.sin(pitch);
            int pitchCos = (int) Math.cos(pitch);
            int yawSin = (int) Math.sin(yaw);
            int yawCos = (int) Math.cos(yaw);
        }
    }*/
}
