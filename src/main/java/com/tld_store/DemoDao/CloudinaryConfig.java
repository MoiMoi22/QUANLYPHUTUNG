package com.tld_store.DemoDao;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dvj8jmk6e",
            "api_key", "684837669696959",
            "api_secret", "x_uDrcstr2TFVO47YgkygjwhR7Q"
        ));
    }
}