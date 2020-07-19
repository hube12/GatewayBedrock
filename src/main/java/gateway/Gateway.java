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
}

public class Gateway {

    static public GatewayChunk getGatewayChunk(int cx, int cz) {
        Random r = new Random(cx * 341873128712L + cz * 132897987541L);
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
        if (args.length<1){
            System.out.println("No argument");
            return;
        }
        long seed;
        try {
            seed = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Wrong seed");
            return;
        }
        run(seed);

    }

    public static void run(long seed) {
        int count=5;
        int r = 10000;
        for (int x = -r; x < r; x++) {
            for (int z = -r; z < r; z++) {
                if (16 * 16 * (x * x + z * z) > 4096) {
                    GatewayChunk c = Gateway.getGatewayChunk(x, z);
                    if (c != null) {
                        if ((c.px < 16) && (c.pz == 23)) {
                            EndBiomeSource source = new EndBiomeSource(MCVersion.v1_16, seed);
                            Biome biome = source.getBiome(16*x, 0, 16*z);
                            if(biome == Biome.END_HIGHLANDS){
                                System.out.println("/tp @p " +16*c.cx + " 90 " + 16*c.cz + " with offset " + c.px + " " + c.py + " " + c.pz);
                                if (count<0){
                                    return;
                                }
                                count--;
                            }

                        }
                    }
                }
            }
        }
    }
}
