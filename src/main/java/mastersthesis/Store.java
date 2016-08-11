package mastersthesis;

import mastersthesis.model.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.toSet;

public class Store {
    
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public Set<Region> regions;
    public Set<Nation> nations;
    public Set<Supplier> suppliers;
    public Set<Customer> customers;
    public Set<Order> orders;
    public Set<Part> parts;
    public Set<PartSupp> partSupps;
    public Set<LineItem> lineItems;
    
    private Stream<String> fileLines(String basePath, String dataFilename) throws IOException {
        return Files.lines(Paths.get(basePath, dataFilename));
    }
    
    public Store(String basePath) throws Exception {
    
        System.out.println("1");
        regions = fileLines(basePath, "region.tbl")
                .map(line -> {
                    String[] split = line.split("\\|");
                    return new Region(
                            parseLong(split[0]),
                            split[1],
                            split[2]
                    );
                })
                .collect(toSet());
        
        nations = fileLines(basePath, "nation.tbl")
                .map(line -> {
                    String[] split = line.split("\\|");
                    Region region = regions.stream()
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
    
        System.out.println("2");
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
                })
                .collect(toSet());
        
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
                })
                .collect(toSet());
        
        orders = fileLines(basePath, "orders.tbl")
                .map(line -> {
                    String[] split = line.split("\\|");
                    Customer customer = customers.stream()
                            .filter(r -> r.custkey == parseLong(split[1]))
                            .findFirst().orElseThrow(RuntimeException::new);
                    return new Order(parseLong(split[0]),
                            customer,
                            split[2],
                            new BigDecimal(split[3]),
                            LocalDate.parse(split[4], FORMATTER),
                            split[5],
                            split[6],
                            parseInt(split[7]),
                            split[8]);
                })
                .collect(toSet());
        
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
                })
                .collect(toSet());
    
        System.out.println("3");
        partSupps = fileLines(basePath, "partsupp.tbl")
                .map(line -> {
                    String[] split = line.split("\\|");
                    Part part = parts.stream()
                            .filter(p -> p.partkey == parseLong(split[0]))
                            .findFirst().orElseThrow(RuntimeException::new);
                    Supplier supplier = suppliers.stream()
                            .filter(s -> s.suppkey == parseLong(split[1]))
                            .findFirst().orElseThrow(RuntimeException::new);
                    
                    return new PartSupp(
                            part,
                            supplier,
                            parseInt(split[2]),
                            new BigDecimal(split[3]),
                            split[4]);
                })
                .collect(toSet());
        
        lineItems = Files.lines(Paths.get(basePath + File.separator + "lineitem.tbl"))
                .map(line -> {
                    String[] split = line.split("\\|");
                    Order order = orders.stream()
                            .filter(o -> o.orderkey == parseLong(split[0]))
                            .findFirst().orElseThrow(RuntimeException::new);
                    PartSupp partSupp = partSupps.stream()
                            .filter(ps -> ps.part.partkey == parseLong(split[1]) && ps.supplier.suppkey == parseLong
                                    (split[2]))
                            .findFirst().orElseThrow(RuntimeException::new);
                    
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
