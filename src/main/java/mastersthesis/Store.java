package mastersthesis;

import mastersthesis.model.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.toSet;

public class Store {
    
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public Map<Long, Region> regions;
    public Set<Nation> nations;
    public Map<Long, Supplier> suppliers;
    public Map<Long, Customer> customers;
    public Map<Long, Order> orders;
    public Map<Long, Part> parts;
    public Map<String, PartSupp> partSupps;
    public Set<LineItem> lineItems;
    
    private Stream<String> fileLines(String basePath, String dataFilename) throws IOException {
        return Files.lines(Paths.get(basePath, dataFilename));
    }
    
    public Store(String basePath) throws Exception {
        
        System.err.println("regions");
        regions = fileLines(basePath, "region.tbl")
                .map(line -> {
                    String[] split = line.split("\\|");
                    return new Region(
                            parseLong(split[0]),
                            split[1],
                            split[2]
                    );
                }).collect(Collectors.toMap(e -> e.regionkey, Function.identity()));
        
        System.err.println("nations");
        nations = fileLines(basePath, "nation.tbl")
                .map(line -> {
                    String[] split = line.split("\\|");
                    Region region = regions.values().stream()
                            .filter(r -> r.regionkey == parseLong(split[2]))
                            .findFirst().orElseThrow(RuntimeException::new);
                    return new Nation(
                            parseLong(split[0]),
                            split[1],
                            region,
                            split[3]
                    );
                })
                .collect(toSet());
        
        System.err.println("suppliers");
        suppliers = fileLines(basePath, "supplier.tbl")
                .map(line -> {
                    String[] split = line.split("\\|");
                    Nation nation = nations.stream()
                            .filter(r -> r.nationkey == parseLong(split[3]))
                            .findFirst().orElseThrow(RuntimeException::new);
                    return new Supplier(
                            parseLong(split[0]),
                            split[1],
                            split[2],
                            nation,
                            split[4],
                            new BigDecimal(split[5]),
                            split[6]
                    );
                }).collect(Collectors.toMap(e -> e.suppkey, Function.identity()));
        
        System.err.println("customers");
        customers = fileLines(basePath, "customer.tbl")
                .map(line -> {
                    String[] split = line.split("\\|");
                    Nation nation = nations.stream()
                            .filter(r -> r.nationkey == parseLong(split[3]))
                            .findFirst().orElseThrow(RuntimeException::new);
                    return new Customer(
                            parseLong(split[0]),
                            split[1],
                            split[2],
                            nation,
                            split[4],
                            new BigDecimal(split[5]),
                            split[6],
                            split[7]);
                }).collect(Collectors.toMap(e -> e.custkey, Function.identity()));
        
        System.err.println("orders");
        orders = fileLines(basePath, "orders.tbl")
                .map(line -> {
                    String[] split = line.split("\\|");
                    Customer customer = customers.get(parseLong(split[1]));
                    return new Order(parseLong(split[0]),
                            customer,
                            split[2],
                            new BigDecimal(split[3]),
                            LocalDate.parse(split[4], FORMATTER),
                            split[5],
                            split[6],
                            parseInt(split[7]),
                            split[8]);
                }).collect(Collectors.toMap(e -> e.orderkey, Function.identity()));
        
        System.err.println("parts");
        parts = fileLines(basePath, "part.tbl")
                .map(line -> {
                    String[] split = line.split("\\|");
                    return new Part(parseLong(split[0]),
                            split[1],
                            split[2],
                            split[3],
                            split[4],
                            parseInt(split[5]),
                            split[6],
                            new BigDecimal(split[7]),
                            split[8]
                    );
                }).collect(Collectors.toMap(e -> e.partkey, Function.identity()));
        
        System.err.println("partSupps");
        partSupps = fileLines(basePath, "partsupp.tbl")
                .map(line -> {
                    String[] split = line.split("\\|");
                    Part part = parts.get(parseLong(split[0]));
                    Supplier supplier = suppliers.get(parseLong(split[1]));
                    
                    return new PartSupp(
                            part,
                            supplier,
                            parseInt(split[2]),
                            new BigDecimal(split[3]),
                            split[4]);
                }).collect(Collectors.toMap(e -> e.id, Function.identity()));
        
        System.err.println("lineItems");
        lineItems = Files.lines(Paths.get(basePath + File.separator + "lineitem.tbl"))
                .map(line -> {
                    String[] split = line.split("\\|");
                    Order order = orders.get(parseLong(split[0]));
                    long partKey = parseLong(split[1]);
                    long suppKey = parseLong (split[2]);
                    PartSupp partSupp = partSupps.get("" + partKey + "," + suppKey);
                    
                    return new LineItem(order,
                            partSupp,
                            parseInt(split[3]),
                            parseInt(split[4]),
                            new BigDecimal(split[5]),
                            new BigDecimal(split[6]),
                            new BigDecimal(split[7]),
                            split[8],
                            split[9],
                            LocalDate.parse(split[10], FORMATTER),
                            LocalDate.parse(split[11], FORMATTER),
                            LocalDate.parse(split[12], FORMATTER),
                            split[13],
                            split[14],
                            split[15]
                    );
                })
                .collect(toSet());
    }
}
