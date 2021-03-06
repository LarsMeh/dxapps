package de.hhu.bsinfo.dxapp;

import de.hhu.bsinfo.dxmem.data.ChunkByteArray;
import de.hhu.bsinfo.dxram.chunk.ChunkLocalService;
import de.hhu.bsinfo.dxram.chunk.ChunkService;
import de.hhu.bsinfo.dxram.migration.LongRange;
import de.hhu.bsinfo.dxutils.NodeID;

import java.util.Random;

public class ChunkHelper {

    private static final Random RANDOM = new Random();

    public static LongRange createChunks(ChunkService p_chunkService, ChunkLocalService p_chunkLocalService, int p_chunkSize, int p_numChunks) {
        return createChunks(p_chunkService, p_chunkLocalService, p_chunkSize, p_numChunks, NodeID.INVALID_ID);
    }

    public static LongRange createChunks(ChunkService p_chunkService, ChunkLocalService p_chunkLocalService, int p_chunkSize, int p_numChunks,  short p_target) {
        long firstChunk = 1L;
        long lastChunk = p_numChunks;

        long then = System.currentTimeMillis();
        for (int j = 0; j < p_numChunks; j++) {
            byte[] bytes = new byte[p_chunkSize];
            RANDOM.nextBytes(bytes);

            ChunkByteArray byteArray = new ChunkByteArray(bytes);

            // Create chunk locally if node id is not valid
            if (p_target == NodeID.INVALID_ID) {
                p_chunkLocalService.createLocal().create(byteArray);
            } else {
                p_chunkService.create().create(p_target, byteArray);
            }

            // TODO(krakowski)
            //  Replace with putLocal once it is implemented
            p_chunkService.put().put(byteArray);

            if (j == 0) {
                firstChunk = byteArray.getID();
            }

            if (j == p_numChunks - 1) {
                lastChunk = byteArray.getID();
            }
        }

        if (firstChunk == 0 || lastChunk == 0) {
            throw new IllegalStateException("Creation of one or more chunks failed");
        }

        return new LongRange(firstChunk, lastChunk + 1);
    }
}
