package pl.nicieja.hibernatewheather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.nicieja.hibernatewheather.model.WeatherLocation;
import pl.nicieja.hibernatewheather.repository.WeatherLocationRepo;

import java.time.LocalDateTime;


@Service
public class WeatherService {

    private WeatherLocationRepo weatherLocationRepo;

    @Autowired
    public WeatherService(WeatherLocationRepo weatherLocationRepo) {
        this.weatherLocationRepo = weatherLocationRepo;
    }

    public Double getTemperature() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String weatherLocation = restTemplate.getForObject("https://www.metaweather.com/api/location/44418/", String.class);
        JsonNode parent = new ObjectMapper().readTree(weatherLocation);
        return Double.parseDouble(parent.get("consolidated_weather").get(0).get("the_temp").toString());
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void retrieveAndSaveTemperature() throws JsonProcessingException {
        WeatherLocation weatherLocation = getWeatherLocation();
        weatherLocationRepo.save(weatherLocation);
    }

    private WeatherLocation getWeatherLocation() throws JsonProcessingException {
        WeatherLocation weatherLocation = new WeatherLocation();
        weatherLocation.setTemperature(getTemperature());
        weatherLocation.setLocation("London");
        weatherLocation.setTimestamp(LocalDateTime.now());
        return weatherLocation;
    }
}
