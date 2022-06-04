package tech.yeez.investment.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.function.ConverterFileName;

/**
 * @author fjtan
 */
public class Generator {

    public static void main(String[] args) {
    	FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/earningfarm?useUnicode=true&characterEncoding=utf-8", "root",
//        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/earningfarm?useUnicode=true&characterEncoding=utf-8", "root",
            "xb910716").globalConfig(builder -> {
                builder.author("xiangbin") //
                    // .enableSwagger() //  swagger
                    .fileOverride() //
                    .commentDate("").outputDir("/Users/xiangbin/temp"); //
            }).packageConfig(builder -> {
                builder.parent("tech.yeez.investment") //
                    // .entity("model.entity.system"); //
                    .entity("model.entity"); //
            }).strategyConfig(builder -> {
                // builder.addInclude("company", "user", "dict", "message", "log"); //
                builder.addInclude("supply","subscribe","transaction_record"); //
            }).templateEngine(new FreemarkerTemplateEngine()) // Freemarker
            .execute();

    }

    static class MyConvertFileName implements ConverterFileName {

        private final String remove;

        public MyConvertFileName(String remove) {
            this.remove = remove;
        }

        @Override
        public String convert(String entityName) {
            return entityName.replaceAll("(?i)" + this.remove, "");
        }
    }
}
