package qa.guru.niffler.utils;

import com.github.javafaker.Faker;

import java.util.List;

public class RandomDataUtils {

    private static final Faker faker = new Faker();

    public static String randomUserName() {
        return faker.name().username();
    }

    public static String randomName() {
        return faker.name().firstName();
    }

    public static String randomSurname() {
        return faker.name().lastName();
    }

    public static String randomCategoryName() {
        return faker.lordOfTheRings().location();
    }

    public static String randomSentence(int wordsCount) {
        List<String> sentence = faker.lorem().sentences(wordsCount);
        return String.join("", sentence);
    }
}
