package pl.nicieja.hibernatewheather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nicieja.hibernatewheather.model.WeatherLocation;

@Repository
public interface WeatherLocationRepo extends JpaRepository<WeatherLocation, Long> {
}
