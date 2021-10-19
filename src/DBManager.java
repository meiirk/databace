import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DBManager{
    //Наш объект connection, который хранит данные по подключению. Вся движуха идет через него. Что-то на подобие Socket.
    private Connection connection;

    public void connect(){
        try{
            //Подтягиваем драйвер, который мы добавили в самом начале урока
            Class.forName("com.mysql.cj.jdbc.Driver");

            //Создаем подключение.
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bitlad_first?useUnicode=true&serverTimezone=UTC","root", ""
            );
            System.out.println("CONNECTED");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public ArrayList<Cars> getAllCars(){
        //Создаем пустой список машин. Позже мы его будем заполнять данными из БД
        ArrayList<Cars> cars = new ArrayList<>();
        try{
            //Готовим наш запрос в базу данных
            PreparedStatement st = connection.prepareStatement("SELECT * FROM cars");

            //При запуске данного запроса мы в ответ получаем объект класса ResultSet. Стоит сохранить его в переменную rs.
            ResultSet rs = st.executeQuery();

            //По сути, ResultSet - это и есть наша таблица. Через цикл мы пробежимся по каждой строке данной таблицы
            while (rs.next()){
                //Получаем данные по каждой колонке. Конвертируем их в привычные нам переменные
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                double engineVolume = rs.getDouble("engine_volume");

                //Добавляем новую машину в список
                cars.add(new Cars(id,name,price,engineVolume));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return cars;
    }

    public void addCar(Cars car){
        try{
            //Готовим наш запрос. В полях VALUES(), оставляем ?, чтобы указать, что туда скоро будут добавлены данные.
            //Это нужно, чтобы мы могли удобно вставить данные объекта car в наш запрос
            PreparedStatement st = connection.prepareStatement("INSERT INTO cars(id, name, price, engine_volume) values(NULL,?,?,?)");

            //Вводим данные машины в эти вопросительные знаки. Сначала указываем номер знака и потом само значение
            st.setString(1,car.getName());
            st.setInt(2,car.getPrice());
            st.setDouble(3,car.getEngineVolume());

            //Запускаем запрос на добавление данных
            st.executeUpdate();

            st.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

