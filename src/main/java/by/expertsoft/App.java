package by.expertsoft;

import by.expertsoft.mycollection.Collection2;
import by.expertsoft.myiterator.AlternateIterator;
import by.expertsoft.mymodel.Matrix;
import by.expertsoft.myrunnable.ThrowingRunnable;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.*;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
        System.out.println("    " + Month.of(month) + " " + Year.of(year));
        IntStream.iterate(1, i -> i + 1).limit(7)
                .forEach(i -> System.out.print(" " + DayOfWeek.of(i).getDisplayName(TextStyle.SHORT, Locale.getDefault())));
        System.out.println();
        Collections.nCopies(LocalDate.of(year, month, 1).getDayOfWeek().getValue() - 1, "    ")
                .forEach(System.out::print);
        IntStream.range(1, 1 + YearMonth.of(year, month).lengthOfMonth()).forEachOrdered((day) -> {
            String prefix = day < 10 ? "   " : "  ";
            if (LocalDate.of(year, month, day).getDayOfWeek() == DayOfWeek.SUNDAY) {
                System.out.println(prefix + day);
            } else {
                System.out.print(prefix + day);
            }
        } );
    }
    //Ex. 5.4: end of function

    //Ex. 5.5: start of function
    public static long getNumberOfDaysYouWasAlive(final LocalDate yourBirthday){
        return Duration.between(yourBirthday.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
    }
    //Ex. 5.5: end of function

    //Ex. 5.9: start of function
    public static Stream<String> getZoneIdsWithNotFullOffset() {
        final LocalDateTime ldt = LocalDateTime.now();
        return ZoneId.getAvailableZoneIds().stream().filter( (zone) -> {
            final ZoneOffset offset = ldt.atZone(ZoneId.of(zone)).getOffset();
            return offset.getTotalSeconds() % (60 * 60) != 0;
        });
    }
    //Ex. 5.9: end of function

    //Ex. 5.11: start of function
    public static Duration calculateHowLongIsFlight(LocalDateTime start, String startZoneId, LocalDateTime finish, String finishZoneId) {
        ZonedDateTime zonedStart = ZonedDateTime.of(start, ZoneId.of(startZoneId));
        ZonedDateTime zonedFinish = ZonedDateTime.of(finish, ZoneId.of(finishZoneId));
        return Duration.between(zonedFinish, zonedStart);
    }
    //Ex. 5.11: end of function

    //Ex. 6.5: start of function
    public static Map<String, Set<File>> getWordAndFilesStatistics(File[] files) {
        final ConcurrentHashMap<String, Set<File>> statistics = new ConcurrentHashMap();
        Stream.of(files).parallel().forEach(file -> {
            try {
                Scanner scanner = new Scanner(file);
                while(scanner.hasNext()) {
                    statistics.merge(scanner.next(), new HashSet<File>(Collections.singletonList(file)), (oldValue, newValue) -> {
                        oldValue.addAll(newValue);
                        return oldValue;
                    });
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        return statistics;
    }
    //Ex. 6.5: end of function

    //Ex. 6.7: start of function
    public static String getKeyWithMaxValue(ConcurrentHashMap<String, Long> map) {
        return map.reduceEntries(1,
                (entry1, entry2) -> entry2.getValue() > entry1.getValue() ? entry2 : entry1)
                .getKey();
    }
    //Ex. 6.7: end of function

    //Ex. 6.9: start of function
    public static long getFn(int n) {
        Matrix[] matrixArray = new Matrix[n];
        Arrays.parallelSetAll(matrixArray, i -> new Matrix(2, 2, new long[] {1, 1, 1, 0}));
        Arrays.parallelPrefix(matrixArray, Matrix::multiple);
        return matrixArray[n - 1].getAt(0, 0);
    }
    //Ex. 6.9: end of function

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
        }   //Ex. 5.1: end

        {   //Ex. 5.4: start
            System.out.println("\nEx.5.4:");
            printCalendar(3, 2013);
            System.out.println();
        }   //Ex. 5.4: end

        {   //Ex. 5.5: start
            System.out.println("\nEx. 5.5:");
            System.out.println("You lived " + getNumberOfDaysYouWasAlive(LocalDate.of(1995, 12, 24)) + " days");
        }   //Ex. 5.5: end

        {   //Ex. 5.9: start
            System.out.println("\nEx. 5.9:");
            getZoneIdsWithNotFullOffset().forEach(System.out::println);
        }   //Ex. 5.9: end

        {   //Ex. 5.11: start
            System.out.println("\nEx. 5.11:");
            System.out.println(calculateHowLongIsFlight(LocalDateTime.of(2018, 2, 1, 14, 5), "America/Los_Angeles",
                    LocalDateTime.of(2018, 2, 1, 16, 40), "Europe/Berlin"));
        }   //Ex. 5.11: end

        {   //Ex. 6.5: start
            System.out.println("\nEx. 6.5:");
            File files[] = {new File("ex_1_4_file_3.txt"), new File("ex_1_4_file_1.txt"),
                    new File("ex_1_4_file_2.txt"), new File("ex_6_5_file_1.txt"),
                    new File("ex_6_5_file_2.txt"), new File("ex_6_5_file_3.txt")};
            getWordAndFilesStatistics(files).forEach((k, v) -> {
                System.out.print(k + ": { ");
                v.forEach(s -> System.out.print(s + ", "));
                System.out.println("}");
            });
        }   //Ex. 6.5: end

        {   //Ex. 6.7: start
            System.out.println("\nEx. 6.7:");
            ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
            map.put("item 1", 1l);  map.put("item 2", -1123l);  map.put("item 3", 1231l);
            map.put("item 4", 111231231231231l);  map.put("item 5", 11231l);  map.put("item 6", 141231231l);
            System.out.println(getKeyWithMaxValue(map));
        }   //Ex. 6.7: end

        {   //Ex. 6.9: start
            System.out.println("\nEx. 6.9:");
            int n = 9;
            System.out.println("n = " + n + ", Fn = " + getFn(n));
        }   //Ex. 6.9: end
    }
}
