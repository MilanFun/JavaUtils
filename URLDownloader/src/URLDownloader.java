/**
 * @author Aleksey Ptukha
 * Program URLDownloader
 * 16.08.2020
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.net.MalformedURLException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

/**
 * Главный класс программы
 */
public class URLDownloader {
    /** Порт URL ссылки */
    private int port;

    /** Протокол URL ссылки */
    private String protocol;

    /** Хост URL ссылки */
    private String host;

    /** Полная имя URL ссылки */
    private String FullNameURL;

    /** Имя файла URL ссылки, которая указана после домена сайта */
    private String FileName;

    /** URL класс */
    private URL hp;

    /**
     * Точка входа программы. На вход принимается массив строк. Первый элемент массива это URL ссылка, по которой
     * программа получает html-страницу, а потом картинки, у которых корректная URL ссылка. Файлы сохраняются в папку
     * проекта. Второй элемент массива входных данных есть путь, по которому можно сохранить файлы.
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        args = new String[2];
        args[0] = "";
        args[1] = "/home/alekseyhp/IdeaProjects/URLDownloader/out/";
        switch(args.length) {
            case 1:
                URLDownloader check1 = new URLDownloader(args[0]);
                System.out.println(check1.toString());
                PageCodeGrabberHTML page1 = new PageCodeGrabberHTML(args[0]);
                page1.getPageCode();

                System.out.print("\n");

                System.out.println("Picture downloading proccess...");
                IMGDownloader check = new IMGDownloader(args[0]);
                check.setListOfURL();
                System.out.print("\n");
                System.out.println("URL quantity of valid picture: " + check.getSize());
                System.out.print("\n");
                System.out.println("List of URL: ");
                check.printList();
                System.out.print("\n");
                System.out.println("Name of picture: ");
                check.getImage();
                System.out.print("\n");
                break;

            case 2:
                URLDownloader check2 = new URLDownloader(args[0]);
                System.out.println(check2.toString());
                PageCodeGrabberHTML page2 = new PageCodeGrabberHTML(args[0], args[1]);
                page2.getPageCode();

                System.out.print("\n");

                System.out.println("Picture downloading proccess...");
                IMGDownloader img = new IMGDownloader(args[0], args[1]);
                img.setListOfURL();
                System.out.print("\n");
                System.out.println("URL quantity of valid picture: " + img.getSize());
                System.out.print("\n");
                System.out.println("List of URL: ");
                img.printList();
                System.out.print("\n");
                System.out.println("Name of picture: ");
                img.getImage();
                System.out.print("\n");
                break;
        }

    }

    /**
     * Конструктор главного класса. Принимает строку, которая является URL ссылкой, которая внутри блока разбирается
     * по частям (с помощью методов класса URL) и присваивается полям класса
     *
     * @param site
     * @throws MalformedURLException
     */
    URLDownloader(String site) throws MalformedURLException {
        if(site == null) {
            throw new NoSuchElementException("There is not web-site url");
        }
        this.FullNameURL = site;
        hp = new URL(site);
        this.port = hp.getPort();
        this.protocol = hp.getProtocol();
        this.host = hp.getHost();
        this.FileName = hp.getFile();
    }

    /**
     * Метод, который выводит поля класса
     * @return
     */
    public String toString() {
        return "Port: " + this.port + "\n" + "Protocol: " + this.protocol + "\n" + "Host: " + this.host + "\n" +
                "Full Name of URL: " + this.FullNameURL + "\n" + "File name: " + this.FileName + "\n";
    }

    /**
     * Геттер главного класса
     * @return FileName
     */
    public String getFileName() {
        return this.FileName;
    }

    /**
     * Геттер главного класса
     * @return Port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Геттер главного класса
     * @return Protocol
     */
    public String getProtocol() {
        return this.protocol;
    }

    /**
     * Геттер главного класса
     * @return FullNameURL
     */
    public String getFullNameURL() {
        return this.FullNameURL;
    }

    /**
     * Геттер главного класса
     * @return host
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Геттер главного класса
     * @return hp
     */
    public URL getClassURL() {
        return this.hp;
    }

}

/**
 * Класс PageCodeGrabberHTML отвечает за загрузку HTML-страницы
 */
class PageCodeGrabberHTML {
    /** Поле, которое хранит URL ссылку*/
    private URL hp = null;

    /** Поле благодаря которому происходит подключение к URL ссылке*/
    private HttpURLConnection conn = null;

    /** Буфер для хранения данных */
    private BufferedReader br = null;

    /** Поле, отвечающее за запись в файл*/
    private OutputStream os = null;
    private String str;

    /** URL адрес*/
    private String website;

    /** Путь до сохраненного файла*/
    private String pathfolder;

    /** Имя сохраняемого файла */
    private String fileN;

    /**
     * Конструктор класса PageCodeGrabberHTML, принимающий только адрес веб-страницы, а место для хранения файлов
     * является папка проекта
     * @param web
     * @throws MalformedURLException
     */
    PageCodeGrabberHTML(String web) throws MalformedURLException {
        this.website = web;
        this.pathfolder = "/home/alekseyhp/IdeaProjects/URLDownloader/";
        this.hp = new URL(web);
    }

    /**
     * Конструктор класса PageCodeGrabberHTML, принимающий адрес веб-страницы и место для хранения файлов
     * @param web
     * @param path
     * @throws MalformedURLException
     */
    PageCodeGrabberHTML(String web, String path) throws MalformedURLException {
        this.website = web;
        this.pathfolder = path;
        hp = new URL(web);
    }

    /**
     * Главный метод класса PageCodeGrabberHTML, который непосредственно сохраняет HTML страницу веб-сайт
     * @throws IOException
     */
    public void getPageCode() throws IOException {
        //Подключение к веб-странице
        conn = (HttpURLConnection) hp.openConnection();

        //Буфер для сохранения данных с веб-страницы, а также с корректной кодировкой для сохранения не только файлов
        //на английском языке, но и на русском
        br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

        //Имя сохраняемого файла
        this.fileN = FTrans.getNameOfFile(hp.getFile());
        try {
            //Запись в файл и в папку
            this.os = new FileOutputStream(new File(this.pathfolder + this.fileN));
            while((this.str = br.readLine()) != null) {
                this.os.write(this.str.getBytes(), 0, this.str.length());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        //Закрытие каналов передачи данных
        os.close();
        br.close();
        System.out.print("Success of downloading HTML of website");

    }

    /**
     * Геттер класса PageCodeGrabberHTML
     * @return fileN
     */
    public String getFileN() {
        return this.fileN;
    }

    /**
     * Геттер класса PageCodeGrabberHTML
     * @return str
     */
    public String getStr() {
        return this.str;
    }

    /**
     * Геттер класса PageCodeGrabberHTML
     * @return hp
     */
    public URL getURL() {
        return this.hp;
    }

    /**
     * Геттер класса PageCodeGrabberHTML
     * @return conn
     */
    public HttpURLConnection getConn() {
        return this.conn;
    }

    /**
     * Геттер класса PageCodeGrabberHTML
     * @return br
     */
    public BufferedReader getBr() {
        return this.br;
    }

    /**
     * Геттер класса PageCodeGrabberHTML
     * @return os
     */
    public OutputStream getOs() {
        return this.os;
    }

    /**
     * Геттер класса PageCodeGrabberHTML
     * @return website
     */
    public String getWebsite() {
        return this.website;
    }

    /**
     * Геттер класса PageCodeGrabberHTML
     * @return pathfolder
     */
    public String getPathfolder() {
        return this.pathfolder;
    }
}


/**
 * Класс для загрузки изображения, а также для записи HTML файла в строку для дальнейшего парсинга для получения URL
 * ссылки для дальнейшей загрузки. Список ссылок хранится в массиве строк.
 */
class IMGDownloader {
    /** Массив, который хранит ссылки на изображения*/
    private String[] ListOfURL;
    private BufferedReader reader = null;
    private URLDownloader tmp = null;

    /** Строка в которой хранится текст html-файла */
    private String html = "";
    private int size;
    private String website;
    private String pathfolder;

    /**
     * Конструктор класса IMGDownloader, принимающий адрес веб-страницы
     * @param inf
     * @throws MalformedURLException
     */
    IMGDownloader(String inf) {
        this.website = inf;
        this.pathfolder = "/home/alekseyhp/IdeaProjects/URLDownloader/";
    }

    /**
     * Конструктор класса IMGDownloader, принимающий адрес веб-страницы и место для хранения файлов
     * @param inf
     * @param way
     * @throws MalformedURLException
     */
    IMGDownloader(String inf, String way) {
        this.website = inf;
        this.pathfolder = way;
    }

    /**
     * Сеттер, который создает массив ссылок на изображения
     * @throws MalformedURLException
     * @throws FileNotFoundException
     */
    public void setListOfURL() throws MalformedURLException, FileNotFoundException {
        //Определение html-строки
        setHtml(this.website);

        //Создаем объект html типа Document, в который парсим html-строку
        Document html = Jsoup.parse(getHtml());

        //Устанавливаем размер
        setSize(html);

        //Создаем список адресов
        ListOfURL = new String[getSize()];

        //Заполняем список
        for(int i = 0; i < ListOfURL.length; i++) {
            //Пробегаемся по циклу и сохраняем элементы с тегом img
            Element k = html.select("img").get(i);

            //Записываем данные, которые указаны в src
            ListOfURL[i] = k.attr("src");
        }
    }

    /**
     * Главный метод класса IMGDownloader, который скачивает и сохраняет в папку изображения
     * @throws IOException
     */
    public void getImage() throws IOException {
        try {
            for (int i = 0; i < this.ListOfURL.length; i++) {
                URL n = new URL(this.ListOfURL[i]);
                String name = FTrans.getNameOfFile(n.getFile());

                System.out.println(getPathfolder() + name);

                InputStream in = n.openStream();
                OutputStream out = new BufferedOutputStream(new FileOutputStream(this.pathfolder + name));

                for (int b; (b = in.read()) != -1; ) {
                    out.write(b);
                }

                out.close();
                in.close();
            }
        } catch (MalformedURLException ex) {
            //Если адрес неверный выкинул сообщение об ошибке
            System.out.println(ex.getMessage() + ":" + "sth wrong with URL");
        }

    }

    /**
     * Сеттер, которые парсит html-файл в строку
     * @param site
     * @throws MalformedURLException
     * @throws FileNotFoundException
     */
    public void setHtml(String site) throws MalformedURLException, FileNotFoundException {
        this.tmp = new URLDownloader(site);
        this.reader = new BufferedReader(new InputStreamReader(
                                         new FileInputStream(this.pathfolder +
                                                 FTrans.getNameOfFile(this.tmp.getFileName())),
                                                                                               StandardCharsets.UTF_8));

        try {
            String line;
            while((line = reader.readLine()) != null) {
                this.html += line;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Геттер класса IMGDownloader
     * @return html
     */
    public String getHtml() {
        return this.html;
    }

    /**
     * Сеттер класса IMGDownloader, который устанавливает сколько всего элементов с тегом img
     * @return
     */
    public void setSize(Document doc) {
        this.size = doc.select("img").size();
    }

    /**
     * Геттер класса IMGDownloader
     * @return size
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Метод для вывода массива ссылок на изображения
     * @return
     */
    public void printList() {
        for(int i = 0; i < this.ListOfURL.length; i++) {
            System.out.println(this.ListOfURL[i]);
        }
    }

    /**
     * Геттер класса IMGDownloader
     * @return ListOfURL
     */
    public String[] getListOfURL() {
        return this.ListOfURL;
    }

    /**
     * Геттер класса IMGDownloader
     * @return website
     */
    public String getWebsite() {
        return this.website;
    }

    /**
     * Геттер класса IMGDownloader
     * @return pathfolder
     */
    public String getPathfolder() {
        return this.pathfolder;
    }
}

/**
 * Абстрактный класс для хранения функций необходимых для выставления корректного имени файла. Если имя файла в URL
 * ссылке содержит "?", то необходимо сделать имя такое, чтобы все что было до "?" было в имени, а все что после не входило в имя файла.
 * Также если имя файла нет, то необходимо установить его по умолчанию - index.html
 */
abstract class FTrans {
    public static String getNameOfFile(String str) {
        if (str.isEmpty()) {
            return "index.html";
        } else {
            return deleteSpecialChar(str);
        }
    }

    public static String deleteSpecialChar(String text) {
        if(text == null) {
            throw new NullPointerException();
        }
        if(text.contains("?")) {
            String tmp1 = text.replace("/", "");
            String tmp2 = tmp1.substring(0, tmp1.indexOf("?"));
            return tmp2;
        } else {
            return text.replace("/", "");
        }
    }
}