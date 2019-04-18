import com.sinosoft.ops.cimp.CimpApplication;
import com.sinosoft.ops.cimp.util.OrganizationUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CimpApplication.class)
public class utilTest {

    @Autowired
    private OrganizationUtil organizationUtil;

    @Test
    public void importTest(){
        organizationUtil.importData();
    }

}
