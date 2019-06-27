package copier.test;

import copier.entity.Source;
import copier.entity.Target;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CopierTest {

    public void testBeanUtilConvert() {
        List<Source> sources = generaSourceList();

        long start = System.currentTimeMillis();

        List<Target> targets = new ArrayList<>(sources.size());
        for (Source source : sources) {
            Target target = new Target();
            BeanUtils.copyProperties(source, target);
            targets.add(target);
        }

        System.out.println("bean-util time: " + (System.currentTimeMillis() - start));
    }


    public void testManuallyConvert() {
        List<Source> sources = generaSourceList();

        long start = System.currentTimeMillis();

        List<Target> targets = new ArrayList<>(sources.size());
        for (Source source : sources) {
            Target target = new Target();
            target.setAge(source.getAge());
            target.setBirthday(source.getBirthday());
            target.setName(source.getName());
            target.setNickName(source.getNickName());
            target.setQq(source.getQq());
            target.setWechat(source.getWechat());
            targets.add(target);
        }

        System.out.println("manually time: " + (System.currentTimeMillis() - start));
    }


    public void testBeanCopierConvert() {
        BeanCopier beanCopier = BeanCopier.create(Source.class, Target.class, false);

        List<Source> sources = generaSourceList();

        long start = System.currentTimeMillis();

        List<Target> targets = new ArrayList<>(sources.size());
        for (Source source : sources) {
            Target target = new Target();
            beanCopier.copy(source, target, null);
            targets.add(target);
        }

        System.out.println("copier time: " + (System.currentTimeMillis() - start));

    }

    public static void main(String[] args) throws InterruptedException {
        CopierTest test = new CopierTest();

        CountDownLatch latch = new CountDownLatch(3);

        Executor executor = Executors.newFixedThreadPool(3);

        executor.execute(() -> {
            CopierTest test1 = new CopierTest();
            test1.testBeanCopierConvert();
            latch.countDown();
        });

        executor.execute(() -> {
            CopierTest test2 = new CopierTest();
            test2.testManuallyConvert();
            latch.countDown();
        });

        executor.execute(() -> {
            CopierTest test3 = new CopierTest();
            test3.testBeanUtilConvert();
            latch.countDown();
        });

        latch.await();

        System.out.println("over");

    }


    private List<Source> generaSourceList() {
        int total = 1000000;
        List<Source> sources = new ArrayList<>(total);

        for (int i = 0; i < total; i++) {
            sources.add(new Source());
        }

        return sources;
    }

}
