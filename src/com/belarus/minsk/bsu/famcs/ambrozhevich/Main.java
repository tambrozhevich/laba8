package com.belarus.minsk.bsu.famcs.ambrozhevich;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    private static List<String> words = new ArrayList<>();
    private static Set<String> wordsSet = new LinkedHashSet<>();
    private static List<List<String>> components = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        readFile("input.txt");
        readFile1("input.txt");
        wordsSet.addAll(words);
        writeToFile("output1.txt", wordsSet.iterator());
        Collections.sort(words, (s1, s2) -> s1.compareTo(s2));
        writeToFile("output2.txt", words.iterator());
        List<Thread> threads = new ArrayList<>();
        for (List<String> list : components) {
            Thread thread = new Thread(() -> Collections.sort(list, (j1, j2) -> j1.compareTo(j2)));
            thread.start();
            threads.add(thread);
        }
        try {
            for (Thread thread : threads)
                thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Collections.sort(components, (l1, l2) -> l1.size() - l2.size());
        int count = findCount();
        FileWriter fw = new FileWriter("output3.txt");
        fw.write(String.valueOf(count));
        fw.close();
        System.out.println(count);
    }

    private static int findCount() {
        int count = 0;
        List<String> first;
        while (!components.isEmpty()) {
            first = components.get(0);
            components.remove(0);
            System.out.print(first.toString());
            while (true) {
                boolean exists = false;
                for (List<String> component : components) {
                    if (isSubList(first, component)) {
                        first = component;
                        System.out.print(first.toString());
                        components.remove(component);
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    System.out.println();
                    break;
                }
            }
            count++;
        }
        return count;
    }

    private static boolean isSubList(List<String> l1, List<String> l2) {
        List<String> example = new ArrayList<>(l2);
        for (int i = 0; i < l2.size(); i++) {
            String fruit = l2.get(i);
            if (!l1.contains(fruit))
                example.remove(fruit);
        }
        return example.size() == l1.size();
    }

    private static void writeToFile(String file, Iterator<?> iterator) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        while (iterator.hasNext()) {
            fileWriter.write(iterator.next().toString());
            fileWriter.write(System.lineSeparator());
        }
        fileWriter.flush();
    }

    private static void readFile(String s) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(s));
        while (scanner.hasNext()) {
            words.add(scanner.next());
        }
    }

    private static void readFile1(String s) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(s));
        while (scanner.hasNextLine()) {
            Scanner component = new Scanner(scanner.nextLine());
            List<String> list = new ArrayList<>();
            while (component.hasNext()) {
                list.add(component.next());
            }
            components.add(list);
        }
    }

}
