package com.gg.core.net;

/**
 * Created by guofeng.qin on 2016/8/19.
 */
public class Test {
    // public static void main(String[] args) {
    //     RPC.Request request = RPC.Request.newBuilder().setIndex(1).setInstance("testinstance").setMethod("testmethod")
    //             .setPayload(ByteString.copyFromUtf8("testpayload")).build();
    //
    //     // JsonFormat jformat = JsonFormat.Printer
    //     JsonFormat.Printer p = JsonFormat.printer();
    //     try {
    //         String jstr = p.print(request);
    //         System.out.println(jstr);
    //
    //         System.out.println("==========================");
    //
    //         JsonFormat.Parser pp = JsonFormat.parser();
    //         RPC.Request.Builder builder = RPC.Request.newBuilder();
    //         pp.merge(jstr, builder);
    //         RPC.Request requ = builder.build();
    //         System.out.println(requ);
    //     } catch (InvalidProtocolBufferException e) {
    //         e.printStackTrace();
    //     }
    //
    //
    // }
}
