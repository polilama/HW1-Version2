package pages;

public class CourseUrlProvider {

    public static String getCourseUrl() {
        String url = System.getenv("COURSE_URL");
        if (url == null || url.isEmpty()) {
            url = "https://otus.ru/catalog/courses";
        }
        return url;
    }
}