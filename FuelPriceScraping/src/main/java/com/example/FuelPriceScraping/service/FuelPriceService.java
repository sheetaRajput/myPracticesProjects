package com.example.FuelPriceScraping.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FuelPriceService {
    

    public void getMainPage() throws IOException {
        Document url = getConnection("https://www.livemint.com/fuel-prices/").get();
        Elements mainForm = url.getElementsByClass("mainSec");
        Elements fuelOptions = mainForm.select("div.fuelFormSec div.radioHolder label h3.radioSec");
        for (Element fuelOption : fuelOptions) {
            System.out.println("Fuel Type: " + fuelOption.text());
            getFuelType(url, mainForm, fuelOption);
        }
    }

    private void getFuelType(Document url, Elements mainForm, Element fuelOption) throws IOException {
        Elements texts = mainForm.select("section.cardHolder.open.fuelBox");
        if (texts.size() >= 4) {
            Element thirdElement = texts.get(2);
            processFuelSection(fuelOption, thirdElement);
            Element fourthElement = texts.get(3);
            processFuelSection(fuelOption, fourthElement);
        } else {
            System.out.println("Insufficient data: Less than 4 sections available.");
        }
    }

    private void processFuelSection(Element fuelOption, Element section) throws IOException {
        Elements forUrlCityOrState = section.select("div.clearfix.fHeadSec h2.fHead.fl strong");
        Elements cityElements = section.select("div.clearfix.fHeadSec h2.fHead.fl");
        for (Element cityElement : cityElements) {
            System.out.println("Type: " + cityElement.text());
            Elements category = section.select("div.tabcontent ul.labelSec li");
            System.out.println("Category: ");
            category.forEach(type -> System.out.println("- " + type.text()));
            getPetrolDetails(fuelOption,forUrlCityOrState);

        }
    }


    private void getPetrolDetails( Element fuelOption, Elements forUrlCityOrState) throws IOException {
        Document connection = getConnection("https://www.livemint.com/fuel-prices/" + fuelOption.text().toLowerCase() + "-" + forUrlCityOrState.text().toLowerCase().replaceAll("\\s", "-")).get();
        Elements petrolDetailsForCity = connection.select("div#petrol_3.tabcontent ul.valueSec  li");
        System.out.println(petrolDetailsForCity.text());
        Elements petrolDetailsForState = connection.select("div#petrol_4.tabcontent ul.valueSec  li");
        System.out.println(petrolDetailsForState.text());
        getDieselData(connection);

    }
    private void getDieselData(Document connection){
        Elements dieselDetailsForCity = connection.select("div#diesel_3.tabcontent ul.valueSec  li");
        System.out.println(dieselDetailsForCity.text());
        Elements dieselDetailsForState = connection.select("div#diesel_4.tabcontent ul.valueSec  li");
        System.out.println(dieselDetailsForState.text());
    }
    private Connection getConnection(String url) {
        return Jsoup.connect(url).timeout(6000);
    }

}
