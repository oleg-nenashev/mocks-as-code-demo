package myproject;

import com.pulumi.Context;
import com.pulumi.Pulumi;
import com.pulumi.asset.FileAsset;
import com.pulumi.aws.ec2.Ec2Functions;
import com.pulumi.aws.ec2.Instance;
import com.pulumi.aws.ec2.InstanceArgs;
import com.pulumi.aws.ec2.SecurityGroup;
import com.pulumi.aws.ec2.SecurityGroupArgs;
import com.pulumi.aws.ec2.inputs.GetAmiArgs;
import com.pulumi.aws.ec2.inputs.GetAmiFilterArgs;
import com.pulumi.aws.ec2.inputs.SecurityGroupIngressArgs;
import com.pulumi.core.Output;
import com.pulumi.aws.s3.BucketObject;
import com.pulumi.aws.s3.BucketObjectArgs;
import com.pulumi.aws.s3.BucketPolicy;
import com.pulumi.aws.s3.BucketPolicyArgs;
import com.pulumi.awsnative.s3.Bucket;
import com.pulumi.awsnative.s3.BucketArgs;
import com.pulumi.awsnative.s3.inputs.BucketWebsiteConfigurationArgs;

import java.util.List;
import java.util.Map;

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
                                .applyValue(bucketArn -> """
                                            {
                                                "Version":"2012-10-17",
                                                "Statement":[{
                                                    "Effect":"Allow",
                                                    "Principal":"*",
                                                    "Action":["s3:GetObject"],
                                                    "Resource":["%s/*"]
                                                }]
                                            }
                                        """.formatted(bucketArn))
                        ).build()
        );


        ctx.export("bucketName", siteBucket.bucketName());
        ctx.export("bucketPolicy", bucketPolicy.policy());
    }
}