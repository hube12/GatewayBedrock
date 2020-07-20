package gateway;

import java.util.Random;

import kaptainwutax.biomeutils.Biome;
import kaptainwutax.biomeutils.source.EndBiomeSource;
import kaptainwutax.seedutils.mc.MCVersion;

class GatewayChunk {

    int cx, cz;
    int px, py, pz;

    public GatewayChunk(int cx, int cz, int px, int py, int pz) {
        this.cx = cx;
        this.cz = cz;
        this.px = px;
        this.py = py;
        this.pz = pz;
    }

    @Override
    public String toString() {
        return "GatewayChunk{" +
                "cx=" + cx +
                ", cz=" + cz +
                ", px=" + px +
                ", py=" + py +
                ", pz=" + pz +
                '}';
    }
}

public class Gateway {

    static public GatewayChunk getGatewayChunk(int cx, int cz) {
        Random r = new Random((long) cx * 341873128712L + (long) cz * 132897987541L);
        int chorus_plants = r.nextInt(5);
        // ignore all chunks with chorus plants
        if (chorus_plants == 0) {
            if (r.nextInt(700) == 0) {
                int px = r.nextInt(16) + 8;
                int pz = r.nextInt(16) + 8;
                int dy = 1 + 3 + r.nextInt(7);
                return new GatewayChunk(cx, cz, px, dy, pz);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("No argument");
            return;
        }

        try {
            long seed = Long.parseLong(args[0]);
            if (args.length == 2) {
                try {
                    run(seed, Integer.parseInt(args[1]));
                } catch (NumberFormatException e) {
                    System.out.println("Wrong max count");
                }

            } else {
                run(seed, 5);
            }
        } catch (NumberFormatException e) {
            System.out.println("Wrong seed");
        }


    }

    public static int sub(int chunkX, int chunkZ, long seed, int count) {
        boolean flag = false;
       /* for (int i = 0; i < 4; i++) {
            int cx = chunkX + (i % 2);
            int cz = chunkZ + (i / 2);*/
        if ((chunkX * chunkX + chunkZ * chunkZ) > 4096) {
            GatewayChunk c = Gateway.getGatewayChunk(chunkX, chunkZ);
            if (c != null) {
                if ((c.px < 16) && (c.pz == 23)) {
                    EndBiomeSource source = new EndBiomeSource(MCVersion.v1_16, seed);
                    Biome biome = source.getBiome(16 * chunkX, 0, 16 * chunkZ);
                    if (biome == Biome.END_HIGHLANDS && !flag) {
                        System.out.println("/tp @p " + 16 * c.cx + " 90 " + 16 * c.cz + " with offset " + c.px + " " + c.py + " " + c.pz);
                        flag = true;
                    }

                }
            }
        }
        // }
        if (flag) {
            count--;
        }
        return count;
    }

    public static void run(long seed, int max_count) {
        int count = max_count;
        count = sub(0, 0, seed, count);
        for (int r = 0; r < 10000; r++) {
            for (int x = -r; x < r; x++) {
                count = sub(x, -r, seed, count);
                count = sub(x, r, seed, count);
            }
            for (int z = -r; z < r; z++) {
                count = sub(-r, z, seed, count);
                count = sub(r, z, seed, count);
            }
            if (count < 0) {
                return;
            }
        }


    }
}
