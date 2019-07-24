package com.hazelcast.demo;

public class Stats {
    private double sum;
    private double sumOfSquares;
    private double count;

    public void registerResult(double metric){
        sum += metric;
        sumOfSquares += Math.pow(metric, 2);
        count +=1;
    }

    public double count(){
        return count;
    }

    public double average(){
        return sum/count;
    }

    public double standardDeviation(){
        return Math.sqrt(sumOfSquares - (Math.pow(sum, 2)/count) );
    }

    public String report(){
        return String.format("%.0f samples with mean=%.1f and standard deviation=%.1f",count, average(), standardDeviation(),sum, sumOfSquares);
    }

}
