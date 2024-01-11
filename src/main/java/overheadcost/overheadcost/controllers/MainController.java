package overheadcost.overheadcost.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import overheadcost.overheadcost.entities.Electricity;
import overheadcost.overheadcost.entities.GasModel;
import overheadcost.overheadcost.entities.LastElectricityRead;
import overheadcost.overheadcost.entities.LastGasModel;
import overheadcost.overheadcost.entities.MonthlyConsumptionStatData;
import overheadcost.overheadcost.services.CommonService;
import overheadcost.overheadcost.services.ElectricityService;
import overheadcost.overheadcost.services.GasService;
import overheadcost.overheadcost.services.LastElectricityReadService;
import overheadcost.overheadcost.services.LastGasReadService;

@Controller
public class MainController {

    private ElectricityService electricityService;
    private LastElectricityReadService lastElectricityReadService;
    private GasService gasService;
    private LastGasReadService lastGasReadService;

    @Autowired
    public void setGasReadService(LastGasReadService lastGasReadService) {
        this.lastGasReadService = lastGasReadService;
    }

    @Autowired
    public void setGasService(GasService gasService) {
        this.gasService = gasService;
    }

    @Autowired
    public void setLastElectricityReadService(LastElectricityReadService lastElectricityReadService) {
        this.lastElectricityReadService = lastElectricityReadService;
    }

    @Autowired
    public void setElectricityService(ElectricityService electricityService) {
        this.electricityService = electricityService;
    }

    @RequestMapping("/")
    public String index(Model model) {

        setModel(model);

        return "index";
    }

    @RequestMapping("/addnewelecdata")
    public String dashboard(Model model) {

        setModel(model);

        return "addnewelecdata";
    }

    @PostMapping("/addNewElectricityData")
    public String addNewElectricityData(@ModelAttribute Electricity newElectricity, Model model) {
        List<LocalDate> localDateList = electricityService.getAllLocalDateFrom();
        boolean isExists = CommonService.containsSameMonthYear(localDateList, newElectricity.getActualDate());

        if (!isExists) {
            var lastElectricityRead = lastElectricityReadService.getLastLastElectricityRead();
            var difference = newElectricity.getT280() - newElectricity.getT180() + lastElectricityRead.getT180()
                    - lastElectricityRead.getT280();
            newElectricity.setDifference((difference < 0) ? 0 : difference);
            electricityService.save(newElectricity);
        }

        setModel(model);
        return "index";
    }

    @PostMapping("/addNewLastElectricityData")
    public String addNewLastElectricityData(@ModelAttribute LastElectricityRead newLastElectricity, Model model) {
        List<LocalDate> localDateList = lastElectricityReadService.getAllLocalDateFrom();
        boolean isExists = CommonService.containsSameMonthYear(localDateList, newLastElectricity.getActualDate());

        if (!isExists) {
            lastElectricityReadService.save(newLastElectricity);
        }

        setModel(model);
        return "index";
    }

    @RequestMapping("/addnewgas")
    public String addNewGas(@ModelAttribute GasModel newGas, Model model) {

        setModel(model);
        return "addnewgasdata";
    }

    @PostMapping("/addnewgasdata")
    public String addNewGasData(@ModelAttribute GasModel newGas, Model model) {
        List<LocalDate> localDateList = gasService.getAllLocalDateFrom();
        boolean isExistsDate = CommonService.containsSameMonthYear(localDateList, newGas.getDate());

        if (!isExistsDate) {
            gasService.save(newGas);
        }
        setModel(model);
        return "index";
    }

    @PostMapping("/addnewlastgasdata")
    public String addNewLastGasData(@ModelAttribute LastGasModel newLastGas, Model model) {
        List<LocalDate> localDateList = lastGasReadService.getAllLocalDateFrom();
        boolean isExists = CommonService.containsSameMonthYear(localDateList, newLastGas.getDate());

        if (!isExists) {
            lastGasReadService.save(newLastGas);
        }

        setModel(model);
        return "index";
    }

     @RequestMapping("/rawdata")
    public String rawData(Model model) {
        model.addAttribute("elecDataList", electricityService.findAll());
        model.addAttribute("gasDataList", gasService.findAll());
        setModel(model);
        return "rawdata";
    }





    private void setModel(Model model) {

        var electricities = electricityService.findAll();
        var lastElectricityRead = lastElectricityReadService.getLastLastElectricityRead();

        List<MonthlyConsumptionStatData> chartDataList = CommonService.getChartData(electricities, lastElectricityRead);
        model.addAttribute("chartDataList", chartDataList);
        model.addAttribute("elecOverHead", electricityService.getLastElectricity(LocalDate.now()).getDifference());
        model.addAttribute("elecPercentage", electricityService.getSellPercentage());

        model.addAttribute("lastElectricityRead", lastElectricityReadService.getLastLastElectricityRead());
        model.addAttribute("lastElectricity", electricityService.getLastElectricity(LocalDate.now()));
        model.addAttribute("newElectricity", new Electricity());
        model.addAttribute("newLastElectricity", new Electricity());

        model.addAttribute("gasOverHead", gasService.getGasConsumptionLastYear());
        model.addAttribute("gasChartDataList", gasService.getGasChartData());
        model.addAttribute("gasDifChartDataList", gasService.getGasDifferenceChartData());
        model.addAttribute("lastGasRead", lastGasReadService.getLastGas(LocalDate.now()).getGas());
        model.addAttribute("lastGas", gasService.getLastGas(LocalDate.now()).getGas());
        model.addAttribute("newGas", new GasModel());
        model.addAttribute("newLastGas", new LastGasModel());

    }

}
