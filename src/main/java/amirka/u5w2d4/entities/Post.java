package amirka.u5w2d4.entities;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "posts")
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    private UUID id;
    private String category;
    private String title;
    private String cover;
    private String content;
    private int readingTime;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    public Post(String category, String title, String content, Author author) {
        Random rnd = new Random();
        this.category = category;
        this.title = title;
        this.cover = "https://picsum.photos/200/300";
        this.content = content;
        this.readingTime = rnd.nextInt(1, 59);
        this.author = author;
    }

    public UUID getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReadingTime() {
        return readingTime;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                ", content='" + content + '\'' +
                ", readingTime=" + readingTime +
                ", author=" + author.getName() +
                '}';
    }
}