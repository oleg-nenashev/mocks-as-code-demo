package myproject;

import com.pulumi.Context;
import com.pulumi.Pulumi;
import com.pulumi.aws.s3.Bucket;
import com.pulumi.aws.s3.BucketPolicy;
import com.pulumi.aws.s3.BucketPolicyArgs;

/**
 * Pulumi application to be deployed.
 * Based on the <a href="https://github.com/pulumi/examples/blob/master/aws-java-webserver">Web Server Using Amazon EC2</a> example.
 */
public class App {
    public static final String BUCKET_NAME = "s3-website-bucket";

    public static void main(String[] args) {
        Pulumi.run(App::stack);
    }

    public static void stack(Context ctx) {
        final var siteBucket = new Bucket(BUCKET_NAME);

        final var bucketPolicy = new BucketPolicy("bucketPolicy",
                BucketPolicyArgs.builder().bucket(siteBucket.getId())
                        .policy(siteBucket.arn()
                                .applyValue("""
                                            {
                                                "Version":"2012-10-17",
                                                "Statement":[{
                                                    "Effect":"Allow",
                                                    "Principal":"*",
                                                    "Action":["s3:GetObject"],
                                                    "Resource":["%s/*"]
                                                }]
                                            }
                                        """::formatted)
                        ).build()
        );

        // TODO: Add SQS

        /* TODO: Add app auth
        var test = new User("test", UserArgs.builder()
                .name("test")
                .path("/test/")
                .build());

        var testAccessKey = new AccessKey("testAccessKey", AccessKeyArgs.builder()
                .user(test.name())
                .build());

        ctx.export("awsIamSmtpPasswordV4", testAccessKey.sesSmtpPasswordV4());
        */

        ctx.export("bucketName", siteBucket.bucket());
        ctx.export("bucketPolicy", bucketPolicy.policy());
    }
}