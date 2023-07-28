package streetalk.demo.v1.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import streetalk.demo.v1.domain.LinkedLocation;
import streetalk.demo.v1.domain.Location;
import streetalk.demo.v1.dto.LocationDto;
import streetalk.demo.v1.dto.NearCity;
import streetalk.demo.v1.dto.kakao.KakaoLocationDto;
import streetalk.demo.v1.dto.kakao.KakaoResponse;
import streetalk.demo.v1.exception.ArithmeticException;
import streetalk.demo.v1.repository.LinkedLocationRepository;
import streetalk.demo.v1.repository.LocationRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class  LocationService {

    private final LocationRepository locationRepository;
    private final LinkedLocationRepository linkedLocationRepository;
    @Value("${kakaoMap.apiKey}")
    private String key;

    @Transactional
    public Location getKoLocation(Double x, Double y){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String apiKey = "KakaoAK " + key;
        headers.set("Authorization", apiKey);
        HttpEntity<String> body = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        LocationDto locationDto = new LocationDto();
        try{
            KakaoResponse kakaoResponse =
                restTemplate.postForObject(new URI("https://dapi.kakao.com/v2/local/geo/coord2regioncode.JSON?x="+x+"&y="+y), body, KakaoResponse.class);
            KakaoLocationDto locationDetail = kakaoResponse.getDocuments().get(0);
            String fullName = locationDetail.getAddress_name();
            Location location = locationRepository.findByFullName(fullName)
                    .orElseGet(()->createLocation(locationDetail));
            return location;
        }catch (HttpClientErrorException e){
            return locationRepository.findById(9999L).orElseThrow();
        } catch (URISyntaxException e) {
            throw new ArithmeticException(404, "While Sending Message Error");
        }
    }

    @Transactional
    public Location createLocation(KakaoLocationDto kakaoLocationDto){
        return locationRepository.save(new Location(
                kakaoLocationDto.getAddress_name(),
                kakaoLocationDto.getRegion_1depth_name(),
                kakaoLocationDto.getRegion_2depth_name(),
                kakaoLocationDto.getRegion_3depth_name()
        ));
    }

    @Transactional
    public List<NearCity> getNearCities(Location location){
        List<NearCity> nearCities = new ArrayList<>();


        List<LinkedLocation> linkedLocations = linkedLocationRepository.findAllByMainLocation(location);
        for (LinkedLocation linkedLocation : linkedLocations) {
            nearCities.add(new NearCity(
                    linkedLocation.getSubLocation().getFullName(),
                    linkedLocation.getSubLocation().getId()
            ));
        }
        return nearCities;
    }





}
