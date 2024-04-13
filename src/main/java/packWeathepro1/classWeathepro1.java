package packWeathepro1;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

//import javax.print.DocFlavor.URL;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



/**
 * Servlet implementation class classWeathepro1
 */
public class classWeathepro1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public classWeathepro1() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String inputData = request.getParameter("userInput");
		//doGet(request, response);
		
		//Api setup
		String apikey = "2bd43ed95073efe9a51354ec8557d961";
		
		//get city name from the user
		String city = request.getParameter("city");
		
		//Take using openweather API
		String apiurl = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apikey;
		
		//API integration
		URL url = new URL(apiurl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		//reading data of the network
		InputStream inputstream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputstream);
		
		//wanted to store in string
		//Scanner sc = new Scanner(reader);
		StringBuilder responsecontent = new StringBuilder();
		
		//to take input from the reader using Scanner Object 
		Scanner sc = new Scanner(reader);
		while(sc.hasNext()) {
			responsecontent.append(sc.nextLine());
		}
		sc.close();
		//System.out.println(responsecontent);
		
		//typecasting and parsing 
		Gson gson = new Gson();
		JsonObject jsonobject = gson.fromJson(responsecontent.toString(), JsonObject.class);
		//System.out.println(jsonobject);
		
		//getting all the things done 
		long dateTimestamp = jsonobject.get("dt").getAsLong()*1000;
		String date = new Date(dateTimestamp).toString();
		
		//temperature 
		double temperatureKelvin = jsonobject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperatureCelsius = (int) (temperatureKelvin-273.15);
		
		//Humdity
		int humidity = jsonobject.getAsJsonObject("main").get("humidity").getAsInt();
		
		//wind speed
		double speed = jsonobject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		//Weather Condition
		String weatherCondition = jsonobject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		//setting the data as the request attributes(For sending it to JSP page)
		request.setAttribute("date", date);
		request.setAttribute("city", city);
		request.setAttribute("temperature", temperatureCelsius);
		request.setAttribute("weatherCondition", weatherCondition);
		request.setAttribute("humidity", humidity);
		request.setAttribute("weatherData",responsecontent.toString());
		request.setAttribute("speed", speed);
		
		connection.disconnect();
		
		//forwarding the request to the weather.jsp page
		request.getRequestDispatcher("index.jsp").forward(request,response);
		
	}

}
