package com.finalexam.DB;

import com.finalexam.Services.JobService;

import javax.ejb.Stateful;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Stateful
public class CreateDB {
//    private static final String JDBC_DRIVER = "org.h2.Driver";
        private static final String JDBC_DRIVER = "org.postgresql.Driver";
//    private static final String DB_URL = "jdbc:h2:mem:~/test";
//    private static final String DB_URL = "jdbc:h2:mem:users";
        private static final String DB_URL = "jdbc:postgresql://localhost:5432/smart_city";
    //  Database credentials
    private static final String USER = "postgres";
    private static final String PASS = "123";

    private Connection conn = null;
    private Statement stmt = null;

    public CreateDB() throws ClassNotFoundException, SQLException {
        // STEP 1: Register JDBC driver
        Class.forName(JDBC_DRIVER);

        // STEP 2: Open a connection
        System.out.println("Connecting to a selected database...");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);

        System.out.println("Connected database successfully...");

        // STEP 3: Execute a query
        stmt = conn.createStatement();
//        conn.setAutoCommit(false);
    }

    // Admin Module
    public List<Users> getAllUsers() throws SQLException {

        String sql = "select * from users";

        ResultSet rs = stmt.executeQuery(sql);
        List<Users> usersList = new ArrayList<>();
        while (rs.next()) {
            System.out.println("-----User-------");
            usersList.add( new Users(
                    rs.getInt("user_id"),
                    rs.getString("fullname"),
                    rs.getString("email"),
                    rs.getString("password"),
                    LocalDate.parse(rs.getString("birthdate")),
                    null
            ));
        }
        return usersList;
    }

    public List<Book> getAllBooks() throws Exception{
        List<Book> bookList = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * from book");

        while (rs.next()){
            bookList.add(new Book(
                    rs.getInt("book_id"),
                    rs.getString("book_title"),
                    rs.getString("genre"),
                    rs.getString("author")
            ));
        }

        return bookList;

    }

    public List<Job> getAllJobs() throws Exception{
        List<Job> jobList = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * from job");

        while (rs.next()){
            jobList.add(new Job(
                    rs.getInt("job_id"),
                    rs.getString("job_name"),
                    null
            ));
        }

        return jobList;

    }

    public List<Film> getAllFilms() throws Exception{
        List<Film> filmList = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * from films");

        while (rs.next()){
            filmList.add(new Film(
                    rs.getInt("film_id"),
                    rs.getString("film_title")
            ));
        }

        return filmList;
    }

    // Post methods
    public void addUser(String fullname, String email, String password, String birthdate) throws SQLException {
        stmt.executeUpdate("insert into users (fullname, email, password, birthdate) " +
                "VALUES ('" + fullname + "','" + email + "','" + password + "','" + birthdate+ "')");
    }

    public void addLibrary(String lib_name, String location) throws SQLException {
        stmt.executeUpdate("insert into library (lib_name, location) " +
                "VALUES ('" + lib_name + "','" + location + "')");
    }

    // Delete methods
    public void deleteUser(int user_id) throws SQLException {
        stmt.executeUpdate("DELETE FROM users WHERE user_id = " + user_id);
    }

    public void deleteBook(int book_id) throws SQLException {
        stmt.executeUpdate("DELETE FROM book WHERE book_id = " + book_id);
    }

    public void deleteFilm(int film_id) throws SQLException {
        stmt.executeUpdate("DELETE FROM films WHERE film_id = " + film_id);
    }

    // Update
    public void editUser(int user_id, String email) throws Exception{
        stmt.executeUpdate(String.format("Update users SET email = '%s' WHERE user_id = %d", email, user_id));
    }




    // <--------------------------------------------------------------------->
    // Student Module
    public List<Library> getAllLibraries()  throws SQLException{
        String sql = "select * from library";
        ResultSet rs = stmt.executeQuery(sql);
        List<Library> libraryList = new ArrayList<>();
        while (rs.next()) {
            System.out.println("-----User-------");
            libraryList.add( new Library(
                    rs.getInt("lib_id"),
                    rs.getString("lib_name"),
                    rs.getString("location")
            ));
        }
        return libraryList;
    }


    public Users getUserById(int id) throws SQLException{
        ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE user_id = " + id);
        Users user = new Users();

        if (rs.next()){
            user.setUser_id(rs.getInt("user_id"));
            user.setBirthdate(LocalDate.parse(rs.getString("birthdate")));
            user.setEmail(rs.getString("email"));
            user.setFullname(rs.getString("fullname"));
            user.setPassword(rs.getString("password"));

            return user;

        } else {
            return null;
        }
    }

    public Library getLibraryById(int id) throws SQLException{
        ResultSet rs = stmt.executeQuery("SELECT * FROM library WHERE lib_id = " + id);
        Library library = new Library();

        if (rs.next()){
            library.setLib_id(rs.getInt("lib_id"));
            library.setLib_name(rs.getString("lib_name"));
            library.setLocation(rs.getString("location"));

            return library;

        } else {
            return null;
        }
    }

    public void setUserToLib(int user_id, int lib_id) throws SQLException{
        try{
            stmt.executeUpdate("INSERT INTO user_lib_j (user_id, lib_id) " +
                    "VALUES ('" + user_id + "','" + lib_id+"')");
            conn.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Map<String, Set<String>> getUserLib(int id) throws SQLException {
        Map<String, Set<String>> usersSetMap = new HashMap<>();
        Set<String> setLib = new HashSet<>();

        ResultSet rs = stmt.executeQuery("SELECT users.fullname, library.lib_name " +
                "from users, library, user_lib_j " +
                "where user_lib_j.user_id = Users.user_id and Library.lib_id = user_lib_j.lib_id " +
                "and Users.user_id = " + id);

        while (rs.next()){
            String value = rs.getString("lib_name");
            setLib.add(value);
            usersSetMap.put(rs.getString("fullname"), setLib);
        }

        return usersSetMap;
    }

    public Map<String, Set<String>> getLibBook(int user_id) throws SQLException {
        Map<String, Set<String>> userBooks = new HashMap<>();

        Set<String> setLib = new HashSet<>();


            ResultSet rs = stmt.executeQuery("SELECT Book.book_title, Users.fullname " +
                    "from Library, Book, Users, lib_book_j, user_lib_j " +
                    "where library.lib_id = lib_book_j.lib_id and lib_book_j.book_id = Book.book_id " +
                    "and user_lib_j.user_id = Users.user_id and Library.lib_id = user_lib_j.lib_id " +
                    "and Users.user_id = " + user_id);
            while (rs.next()){
                String value = rs.getString("book_title");
                setLib.add(value);

                userBooks.put(rs.getString("fullname"), setLib);
            }

        return userBooks;
    }

    // JobController
    public List<JobCenter> getAllCenters()  throws SQLException{
        String sql = "select * from job_center";
        ResultSet rs = stmt.executeQuery(sql);
        List<JobCenter> jobCenterList = new ArrayList<>();
        while (rs.next()) {
            jobCenterList.add( new JobCenter(
                    rs.getInt("job_center_id"),
                    rs.getString("center_name"),
                    rs.getString("center_location"),
                    rs.getString("center_contact")
            ));
        }
        return jobCenterList;
    }

    public List<Job> getJobsByCenterID(int center_id) throws SQLException{
        ResultSet rs = stmt.executeQuery("SELECT Job_Center_j.Job_Center_j, Job.job_name, Job_Center_j.job_salary " +
                "from Job, Job_center, Job_center_j " +
                "where Job_Center_j.job_id = Job.job_id and Job_Center_j.job_center_id = Job_center.job_center_id " +
                "and Job_center.job_center_id = " + center_id);


        List<Job> jobList = new ArrayList<>();

        while (rs.next()) {
            jobList.add( new Job(
                    rs.getInt("job_Center_j"),
                    rs.getString("job_name"),
                    BigDecimal.valueOf(rs.getDouble("job_salary"))
            ));
        }
        return jobList;
    }

    public Users requestJobByID(int job_id, int user_id) throws Exception{
        stmt.executeUpdate(String.format("UPDATE users SET fk_job_id = %d WHERE user_id = %d", job_id, user_id));
        Statement new_st = conn.createStatement();
        ResultSet rs = new_st.executeQuery("SELECT Job_Center_j.job_Center_j, Job.job_name, Job_Center_j.job_salary " +
                "from Job, Job_center_j, Users " +
                "where Users.fk_job_id = Job_center_j.Job_center_j and " +
                "Job_Center_j.job_id = Job.job_id and " +
                "Users.user_id = " + user_id);

        Users users = getUserById(user_id);

        if(rs.next()){
            users.setJob(new Job(
                    rs.getInt("Job_Center_j"),
                    rs.getString("job_name"),
                    BigDecimal.valueOf(rs.getDouble("job_salary"))
            ));

        }
        return users;

    }

    //-----------------------------------------------------------
    // Tourism Module
    public List<Cinema> viewAllCinemas () throws SQLException {
        List<Cinema> cinemaList = new ArrayList<>();
        String sql = "select * from cinema";
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            cinemaList.add( new Cinema(
                    rs.getInt("cin_id"),
                    rs.getString("cin_name"),
                    rs.getString("cin_add")
            ));
        }
        return cinemaList;
    }

    public Map<String, List<Session>> viewAllSession(int cin_id) throws Exception{
        Map<String, List<Session>> listMap= new HashMap<>();
        List<Session> sessionList = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT Session.ses_id, Cinema.cin_name, Films.film_title, Session.start, seat, row, day, istaked " +
                "from Films, Session, Cinema " +
                "where Session.cin_id = Cinema.cin_id and Films.film_id = Session.film_id " +
                "and Cinema.cin_id = " + cin_id);

        while (rs.next()) {
            sessionList.add( new Session(
                    rs.getInt("ses_id"),
                    rs.getString("start"),
                    rs.getString("film_title"),
                    rs.getInt("seat"),
                    rs.getInt("row"),
                    rs.getInt("day"),
                    rs.getBoolean("istaked")
            ));

            listMap.put(rs.getString("cin_name"), sessionList);
        }

        return listMap;
    }

    public Map<String, List<Session>> buyTicket(int user_id, int ses_id) throws Exception{
        Map<String, List<Session>> listMap= new HashMap<>();
        List<Session> sessionList = new ArrayList<>();

        stmt.executeUpdate(String.format("UPDATE session SET user_id = %d, istaked = true WHERE ses_id = %d and istaked = false ", user_id, ses_id));

        Statement new_st = conn.createStatement();
        ResultSet rs = new_st.executeQuery("SELECT Session.ses_id, Users.fullname,  Cinema.cin_name, Films.film_title, Session.start, seat, row, day, istaked " +
                "from Films, Session, Cinema, Users " +
                "where Session.cin_id = Cinema.cin_id and Films.film_id = Session.film_id and " +
                "Users.user_id = Session.user_id and " +
                "Users.user_id = " + user_id);

        while (rs.next()) {
            sessionList.add( new Session(
                    rs.getInt("ses_id"),
                    rs.getString("start"),
                    rs.getString("film_title"),
                    rs.getInt("seat"),
                    rs.getInt("row"),
                    rs.getInt("day"),
                    rs.getBoolean("istaked")
            ));

            listMap.put(rs.getString("fullname"), sessionList);
        }

        return listMap;
    }

    //<--------------------------------------------------->
    // Business Module
    public List<Lodging> getAllLodges() throws Exception{
        List<Lodging> lodgingList = new ArrayList<>();

        ResultSet rs = stmt.executeQuery("SELECT * FROM lodging");

        while(rs.next()){
            lodgingList.add(new Lodging(
                    rs.getInt("lod_id"),
                    rs.getString("lod_name"),
                    rs.getString("lod_location")
            ));
        }

        return lodgingList;

    }

    public Map<String, List<Premises>> getPremises(int lod_id) throws Exception{
        Map<String, List<Premises>> premisesMap = new HashMap<>();
        List<Premises> premisesList = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("select lodging_user_j.lod_user_id, lod_name, house_name, ap_num, ap_cash " +
                "from Lodging, lodging_user_j, Apartment, House " +
                "where lodging.lod_id = lodging_user_j.lod_id and apartment.ap_id = lodging_user_j.ap_id " +
                "and House.house_id = lodging_user_j.house_id " +
                "and Lodging.lod_id = " + lod_id);

        while (rs.next()){
            premisesList.add(new Premises(
                    rs.getInt("lod_user_id"),
                    rs.getString("house_name"),
                    rs.getInt("ap_num"),
                    rs.getBigDecimal("ap_cash")
            ));

            premisesMap.put(rs.getString("lod_name"), premisesList);
        }

        return premisesMap;
    }

    public Map<String, List<Premises>> setPremise(int user_id, int pr_id) throws Exception{
        Map<String, List<Premises>> premisesMap = new HashMap<>();
        List<Premises> premisesList = new ArrayList<>();

        try {
            stmt.executeUpdate("update lodging_user_j set user_id = " + user_id + " where lod_user_id = " + pr_id);

            Statement new_st2 = conn.createStatement();

            ResultSet rs = new_st2.executeQuery("select Users.fullname, lodging_user_j.lod_user_id, house_name, ap_num, ap_cash " +
                    "from Lodging, lodging_user_j, Apartment, House, Users " +
                    "where lodging_user_j.ap_id = apartment.ap_id and users.user_id = lodging_user_j.user_id " +
                    "and house.house_id = lodging_user_j.house_id and Lodging.lod_id = lodging_user_j.lod_id " +
                    "and Users.user_id = " + user_id + " and lodging_user_j.lod_user_id = " + pr_id);

            while (rs.next()){
                premisesList.add( new Premises(
                        rs.getInt("lod_user_id"),
                        rs.getString("house_name"),
                        rs.getInt("ap_num"),
                        rs.getBigDecimal("ap_cash")
                ));
                premisesMap.put(rs.getString("fullname"), premisesList);

            }

            return premisesMap;

        } catch (Exception e){
            return null;
        }

    }

    public Map<String, String> getCheckList(int user_id) throws Exception{
        Map<String, String> checkMap = new HashMap<>();


        ResultSet rs = stmt.executeQuery("select count(lodging_user_j.user_id) as Premises_count, sum(ap_cash) as Total_Cash " +
                "from lodging_user_j, Apartment " +
                "where lodging_user_j.ap_id = apartment.ap_id and user_id = " + user_id);

        if(rs.next()){
            checkMap.put("Premises_count: ", rs.getString("Premises_count"));
            checkMap.put("Total Cash: ", rs.getString("Total_Cash"));
        }

        return checkMap;

    }


    // Get User by name, get user password

    public String getUserPassword(String email) throws Exception{
        ResultSet rs = stmt.executeQuery("SELECT password FROM users WHERE email = '" + email + "'");

        if(rs.next()) {
            return rs.getString("password");
        }
        else
            return null;

    }


}
