package NewHostelLayer.HostelLayer.Controller;


import NewHostelLayer.HostelLayer.Entity.HostelSource;
import NewHostelLayer.HostelLayer.Service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SourceController
{

    @Autowired
    SourceService sourceService;

    @PostMapping("/createSource")
    public ResponseEntity<HostelSource> createSource(@RequestBody HostelSource source,
                                                     @RequestParam String role,
                                                     @RequestParam String email)
    {
        HostelSource source1 = sourceService.createSource(source, role, email);
        return ResponseEntity.ok(source1);
    }

    @PutMapping("/updateSource/{id}")
    public ResponseEntity<HostelSource> updateSource(@PathVariable Long id,
                                                     @RequestBody HostelSource source,
                                                     @RequestParam String role,
                                                     @RequestParam String email)
    {
        HostelSource source1 = sourceService.updateSource(id, source, role, email);
        return ResponseEntity.ok(source1);

    }

    @GetMapping("/getSourceById/{id}")
    public ResponseEntity<HostelSource> getSourceById(@PathVariable Long id, @RequestParam String role,
                                                      @RequestParam String email)
    {
        HostelSource source = sourceService.getSourceById(id, role, email);
        return ResponseEntity.ok(source);
    }

    @GetMapping("/getAllSource")
    public ResponseEntity<List<HostelSource>> getAllSource(@RequestParam String role,
                                                           @RequestParam String email)
    {
        List<HostelSource> source = sourceService.getAllSourceByBranchCode(role, email);
        return ResponseEntity.ok(source);
    }

    @DeleteMapping("/deleteSource/{id}")
    public ResponseEntity<String> deleteSource(@PathVariable Long id, @RequestParam String role, @RequestParam String email)
    {
        sourceService.deleteSource(id, role, email);
        return ResponseEntity.ok("Source Delete SuccessFully");
    }
}
