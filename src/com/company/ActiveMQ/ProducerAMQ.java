package com.company.ActiveMQ;

/**
 * Created by EdoTyran on 6/25/2017.
 */

import com.company.InsertionSort;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.company.Util.FileUtil.readFile;
import static com.company.Util.FileUtil.writeFile;

public class ProducerAMQ {
    private static final int AMOUNT_OF_BUCKETS = 100;

    // either connect to the remote ActiveMQ running on the PI, or on the localhost
    private static String url = "failover:(tcp://EdoTyran-PC:61616,localhost:8161)";
    private static String subject = "testQueue1"; // Queue Name
    protected static ArrayList<ArrayList<Integer>> data;

    public ProducerAMQ(ArrayList<ArrayList<Integer>> data) {
        this.data = data;
    }

    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(subject);
        MessageProducer producer = session.createProducer(destination);

        //Reading input file
        Integer[] input = new Integer[0];
        try {
            input = readFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Starting to fill buckets");

        //Creating empty bucketList
        ArrayList<String> bucketlist = new ArrayList<>();
        for (int i = 0; i < AMOUNT_OF_BUCKETS; i++) {
            bucketlist.add("");
        }

        //Inserting data into bucketList unsorted
        for (int i = 0; i < input.length; i++) {
            int value = input[i];
            int key = (int) Math.sqrt(value) - 1;

            String newString = bucketlist.get(key) + value + " ";

            bucketlist.set(key, newString);
        }
        //Sending messages with buckets
        for (int i = 0; i < AMOUNT_OF_BUCKETS; i++) {
            String str = bucketlist.get(i);
            TextMessage message = session.createTextMessage(str.substring(0, str.length() - 1));
            producer.send(message);
        }

        //System.out.println("Sent Message '" + message.getText() + "'");
        connection.close();
    }

    public static String arrayToString(int[] a) {
        StringBuilder builder = new StringBuilder();
        for (int i : a) {
            builder.append(String.format("%s ", i));
        }
        return String.join(" ", builder.toString());
    }
}

class SortStringfromQueue {
    private static final int AMOUNT_OF_BUCKETS = 50;

    // either connect to the remote ActiveMQ running on the PI, or on the localhost
    private static String url = "failover:(tcp://EdoTyran-PC:61616,localhost:8161)";
    private static String subjectFrom = "testQueue1";
    private static String subjectTo = "testQueue2";
    public static void main(String args[]) throws Exception {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination_fromQueue = session.createQueue(subjectFrom);
        MessageConsumer consumer = session.createConsumer(destination_fromQueue);
        while(true) {
            Message message = consumer.receive();

            Integer[] integers = null; // to hold the converted and sorted numbers
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String str = textMessage.getText();
                String[] integerStrings = str.split(" ");  //to store the string of numbers retrieved from the queue
                integers = new Integer[integerStrings.length];
                for (int j = 0; j < integers.length; j++) {
                    integers[j] = Integer.parseInt(integerStrings[j]);
                }
            }
            Destination destination_toQueue = session.createQueue(subjectTo);
            MessageProducer producer = session.createProducer(destination_toQueue);

            InsertionSort insertionSort = new InsertionSort("sorter");
            insertionSort.setNum(integers);
            insertionSort.run();

            integers = insertionSort.getNum();

            String stringForConsumer = Arrays.toString(integers);
            TextMessage messageTo = session.createTextMessage(stringForConsumer);
            producer.send(messageTo);
        }

        //connection.close();
    }
}


class ConsumerAMQ {
    private static final int AMOUNT_OF_BUCKETS = 100;

    // either connect to the remote ActiveMQ running on the PI, or on the localhost
    private static String url = "failover:(tcp://EdoTyran-PC:61616,localhost:8161)";
    private static String subject = "testQueue2";
    private static long start, end;

    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(subject);
        MessageConsumer consumer = session.createConsumer(destination);
        ArrayList<Integer[]> sortedList = new ArrayList<>();
        for (int i = 0; i < AMOUNT_OF_BUCKETS; i++) {
            Message message = consumer.receive();
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String str = textMessage.getText().substring(1, textMessage.getText().length() - 1);
                String[] integerStrings = str.split(", ");  //to store the string of numbers retrieved from the queue
                sortedList.add(new Integer[integerStrings.length]);
                for (int j = 0; j < integerStrings.length; j++) {
                    sortedList.get(i)[j] = Integer.parseInt(integerStrings[j]);
                }
            }
        }

        try {
            writeFile(sortedList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println("done!");
        endTime();
        connection.close();

        double estimatedTime = (end - start) / 1000000000.0;

        System.out.print("\nTime: " + estimatedTime);
    }

    public static void startTime(){
        start = System.nanoTime();
    }

    public static void endTime(){
        end = System.nanoTime();
    }
}

