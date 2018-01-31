package by.expertsoft;

import by.expertsoft.mycollection.Collection2;
import by.expertsoft.myiterator.AlternateIterator;
import by.expertsoft.myrunnable.ThrowingRunnable;

import java.io.File;
import java.time.*;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class App {

    //Ex. 1.4: start of function
    public static void sortFiles(File[] files) {
        Arrays.sort(files, (file1, file2) -> {
            if (file1.isDirectory() == file2.isDirectory()) {
                return file1.getPath().compareTo(file2.getPath());
            } else {
                return file1.isDirectory() ? -1 : 1;
            }
        });
    }
    //Ex. 1.4: end of function

    //Ex. 1.6: start of function
    public static Runnable uncheck(ThrowingRunnable f) {
        return () -> {
            try {
                f.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } catch (Throwable t) {
                throw t;
            }
        };
    }
    //Ex. 1.6: end of function

    //Ex. 2.6: start of function
    public static Stream<Character> characterStream(String s) {
        return s.chars().mapToObj(i -> (char) i);
    }
    //Ex. 2.6: end of function

    //Ex. 2.8: start of function
    public static <T> Stream<T> zip(Stream<T> first, Stream<T> second) {
        Iterable<T> iterable = () -> new AlternateIterator(first.iterator(), second.iterator());
        return StreamSupport.stream(iterable.spliterator(), false);
    }
    //Ex. 2.8: end of function

    //Ex. 2.9: start of function
    public static <T>ArrayList<T> joinStreamOfArrayLists(Stream<ArrayList<T>> arrayListStream) {
        return arrayListStream.reduce(new ArrayList<T>(), (x, y) -> {x.addAll(y); return x;});
    }
    //Ex. 2.9: end of function

    //Ex. 2.10: start of functions
    public static Double getAverageForStream(Stream<Double> stream) {
        return stream.collect(Collectors.averagingDouble(x -> (Double) x));
    }

    public static Double getAverageForStreamViaStatistics(Stream<Double> stream) {
        return stream.collect(Collectors.summarizingDouble(x -> (Double) x)).getAverage();
    }
    //Ex. 2.10: end of functions

    //Ex. 2.12: start of function
    public static AtomicIntegerArray getStatisticOfShortWords(int maxLength, Stream<String> words) {
        AtomicIntegerArray shortWords = new AtomicIntegerArray(maxLength);
        words.parallel().forEach(
                word -> {
                    if (word.length() < maxLength) {
                        shortWords.getAndIncrement(word.length());
                    }
                }
        );
        return shortWords;
    }
    //Ex. 2.12: end of function

    //Ex. 2.13: start of function
    public static Map<Integer, Long> getStatisticOfShorWordsUsingFilter(int maxlength, Stream<String> words){
        return words.parallel().filter(word -> word.length() < maxlength)
                .collect(Collectors.groupingBy(String::length, Collectors.counting()));
    }
    //Ex. 2.13: end of function

    //Ex. 5.1: start of function
    public static LocalDate getProgrammersDay(LocalDate localDate) {
        return LocalDate.ofYearDay(localDate.getYear(), 256);
    }
    //Ex. 5.1: end of function

    //Ex. 5.4: start of function
    public static void printCalendar(int month, int year) {
        System.out.println(Month.of(month) + " " + Year.of(year));
        IntStream.iterate(1, i -> i + 1).limit(7)
                .forEach(i -> System.out.print(" " + DayOfWeek.of(i).toString().substring(0, 2)));
        System.out.println();
        Collections.nCopies(LocalDate.of(year, month, 1).getDayOfWeek().getValue() - 1, "   ")
                .forEach(System.out::print);
        IntStream.range(1, 1 + YearMonth.of(year, month).lengthOfMonth()).forEach((day) -> {
            LocalDate dayOfMonth = LocalDate.of(year, month, day);
            String prefix = day < 10 ? "  " : " ";
            if (dayOfMonth.getDayOfWeek() == DayOfWeek.SUNDAY) {
                System.out.println(prefix + day);
            } else {
                System.out.print(prefix + day);
            }
        } );
    }
    //Ex. 5.4: end of function

    public static void main( String[] args ) throws InterruptedException {
        {   //Ex. 1.4: start

            File files[] = {new File("ex_1_4_file_3.txt"), new File("ex_1_4_file_1.txt"),
                    new File("src"), new File(".idea"), new File("ex_1_4_file_2.txt")};
            System.out.println("Ex. 1.4 before sort: ");
            Stream.of(files).map(File::getPath).forEach(System.out::println);
            sortFiles(files);
            System.out.println("\nEx. 1.4 after sort: ");
            Stream.of(files).map(File::getPath).forEach(System.out::println);

        }   //Ex. 1.4: end


        {   //Ex. 1.6: start
            System.out.println("\nEx. 1.6: ");
            new Thread(uncheck(
                () -> {
                    System.out.println("Zzz");
                    Thread.sleep(1000);
                })).start();
            Thread.sleep(1000);
        }   //Ex. 1.6: end

        {   //Ex. 2.6: start
            System.out.println("\nEx. 2.6: ");
            Stream<Character> result = characterStream("test string");
            result.forEach(System.out::println);
        }   //Ex. 2.6: end

        {   //Ex. 2.8: start
            System.out.println("\nEx. 2.8: ");
            Stream<Character> streamOne = characterStream("135");
            Stream<Character> streamTwo = characterStream("2468");
            zip(streamOne, streamTwo).forEach(System.out::print);
            System.out.println();
        }   //Ex. 2.8: end

        {   //Ex. 2.9: start
            System.out.println("\nEx.2.9:");
            Stream<ArrayList<String>> arrayListStream = Stream.of(
                    new ArrayList<>(Arrays.asList("hello", "world")),
                    new ArrayList<>(Arrays.asList("hello1", "world1")),
                    new ArrayList<>(Arrays.asList("hello2", "world2"))
            );
            joinStreamOfArrayLists(arrayListStream).stream().forEach(System.out::println);
        }   //Ex. 2.9: end

        {   //Ex. 2.10: start
            Stream<Double> streamOne = Stream.of(1.0, 2.0, 3.0, 4.0);
            Stream<Double> streamTwo = Stream.of(1.0, 2.0, 3.0, 4.0);
            System.out.println("\nEx.2.10:");
            System.out.println("using averaging: " + getAverageForStream(streamOne));
            System.out.println("using statistics: " + getAverageForStreamViaStatistics(streamTwo));
        }   //Ex. 2.10: end

        {   //Ex. 2.12: start
            Stream<String> words = Stream.of("word", "word 2", "word 111", "word word 1", "word word 11", "word word word word");
            AtomicIntegerArray shortWords = getStatisticOfShortWords(12, words);
            System.out.println("\nEx.2.12:");
            System.out.println(shortWords);
        }   //Ex. 2.12: end

        {   //Ex. 2.13: start
            Stream<String> words = Stream.of("word", "word 2", "word 111", "word word 1", "word word 11", "word word word word");
            Map<Integer, Long> result = getStatisticOfShorWordsUsingFilter(12, words);
            System.out.println("\nEx.2.13:");
            result.forEach((k, v) -> System.out.println("word with length " + k + " : " + v));
        }   //Ex. 2.13: end

        {   //Ex. 5.1: start
            System.out.println("\nEx.5.1:");
            LocalDate programmersDay = getProgrammersDay(LocalDate.of(2014, 1, 1));
            System.out.println(programmersDay);
            System.out.println();
        }   //Ex. 5.1: end

        {   //Ex. 5.4: start
            System.out.println("\nEx.5.4:");
            printCalendar(3, 2013);
            System.out.println();
        }   //Ex. 5.4: end

    }
}
