
package mapreduce.reducesidejoin;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class JoinReducer extends Reducer<AirportKey, Text, Text, Text> {
    @Override
    protected void reduce(AirportKey key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Iterator<Text> iter = values.iterator();
        Text name = new Text(iter.next());
        double maxDelay = 0, minDelay = Double.MAX_VALUE, averageDelay=0, sum=0;
        int count=0;
        double delay;
        while (iter.hasNext()) {
            delay = Double.parseDouble(iter.next().toString());
            if(delay > maxDelay)
                maxDelay=delay;
            if(delay < minDelay)
                minDelay = delay;
            count++;
            sum += delay;
        }
        if (count==0)
            return;
        averageDelay=sum/count;
        String delayInfo =minDelay+" "+String.format("%.2f",averageDelay)+" "+maxDelay;
        context.write(name, new Text(delayInfo));
    }
}