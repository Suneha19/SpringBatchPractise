package com.qiwkreport.qiwk.etl.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

public class RangePartitioner implements Partitioner {

	private static final Logger LOGGER = LoggerFactory.getLogger(RangePartitioner.class);

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		LOGGER.info("partition called gridsize= " + gridSize);

		Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();

		int range = 1;
		int fromId = 1;
		int toId = range;

		for (int i = 1; i <= gridSize; i++) {
			ExecutionContext value = new ExecutionContext();

			System.out.println("\nStarting : Thread" + i);
			System.out.println("fromId : " + fromId);
			System.out.println("toId : " + toId);

			value.putInt("fromId", fromId);
			value.putInt("toId", toId);

			// give each thread a name, thread 1,2,3
			value.putString("name", "Thread" + i);

			result.put("partition" + i, value);

			fromId = toId + 1;
			toId += range;

		}
		return result;
	}
}
