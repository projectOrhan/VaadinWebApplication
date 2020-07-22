package com.vaadin.assignment.backend.service;

import com.vaadin.assignment.backend.entity.BirthCity;
import com.vaadin.assignment.backend.entity.Customer;
import com.vaadin.assignment.backend.repository.BirthCityRepository;
import com.vaadin.assignment.backend.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CustomerService {
    private static final Logger LOGGER = Logger.getLogger(CustomerService.class.getName());
    private CustomerRepository customerRepository;
    private BirthCityRepository birthCityRepository;

    public CustomerService(CustomerRepository customerRepository,
                           BirthCityRepository birthCityRepository) {
        this.customerRepository = customerRepository;
        this.birthCityRepository = birthCityRepository;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public List<Customer> findAll(String filterText) {
        if(filterText == null || filterText.isEmpty()) {
            return customerRepository.findAll();
        } else  {
            return  customerRepository.search(filterText);
        }
    }

    public long count() {
        return customerRepository.count();
    }

    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    public void save(Customer customer) {
        if (customer == null) {
            LOGGER.log(Level.SEVERE,
                    "Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        customerRepository.save(customer);
    }

    //81 il.
    @PostConstruct
    public void populateTestData() {
        if (birthCityRepository.count() == 0) {
            birthCityRepository.saveAll(
                    Stream.of("Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Aksaray", "Amasya",
                            "Ankara", "Antalya", "Ardahan", "Artvin", "Aydın", "Balıkesir", "Bartın",
                            "Batman", "Bayburt", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa",
                            "Çanakkale", "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Düzce", "Edirne",
                            "Elazığ", "Erzincan", "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane",
                            "Hakkâri", "Hatay", "Iğdır", "Isparta", "İstanbul", "İzmir", "Kahramanmaraş",
                            "Karabük", "Karaman", "Kars", "Kastamonu", "Kayseri", "Kilis", "Kırıkkale",
                            "Kırklareli", "Kırşehir", "Kocaeli", "Konya", "Kütahya", "Malatya", "Manisa",
                            "Mardin", "Mersin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Osmaniye",
                            "Rize", "Sakarya", "Samsun", "Şanlıurfa", "Siirt", "Sinop", "Sivas", "Şırnak",
                            "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Uşak", "Van", "Yalova", "Yozgat", "Zonguldak")
                            .map(BirthCity::new)
                            .collect(Collectors.toList()));
        }


        //başlangıçta oluşturulan test profiller.
        if (customerRepository.count() == 0) {
            Random r = new Random(0);
            List<BirthCity> companies = birthCityRepository.findAll();
            customerRepository.saveAll(
                    Stream.of("Ahmet Canatan", "Hakkı Hakalmaz", "Fulya Güneş",
                            "Mehmet Çetin", "Canan Açmaz", "Fırat Kaya", "Kerem Can", "Hikmet Uyan",
                            "Gizem Gizemli", "Ecrin Nur", "Mert Eski", "Ertan Yaşar", "İhsan Varol",
                            "Büşra Cengiz", "Cansu Kale", "Mikail Ateş", "Feridun Özcan", "Osman Taş",
                            "Can Berk", "Dudu Keskin", "Sinan Kaş", "Kerim Abdulcabbar", "Cengiz Tabak",
                            "Ahmet Dönmez", "Ali Döner", "İbrahim Kısa", "İsmail Tan", "Samet Yancı",
                            "Pelin Su", "Yusuf Tezcan")
                            .map(name -> {
                                String[] split = name.split(" ");
                                Customer customer = new Customer();
                                customer.setFirstName(split[0]);
                                customer.setLastName(split[1]);
                                customer.setBirthDate(createRandomDate());
                                customer.setBirthCity(companies.get(r.nextInt(companies.size())));
                                customer.setFlag(Customer.Flag.values()[r.nextInt(Customer.Flag.values().length)]);
                                customer.setGender(Customer.Gender.values()[r.nextInt(Customer.Gender.values().length)]);
                                return customer;
                            }).collect(Collectors.toList()));
        }
    }

    private LocalDate createRandomDate() {
        long minDay = LocalDate.of(1950, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2002, 7, 22).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return randomDate;
    }
}