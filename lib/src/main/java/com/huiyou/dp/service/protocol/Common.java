// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: common.proto

package com.huiyou.dp.service.protocol;

public final class Common {
  private Common() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }
  /**
   * Protobuf enum {@code com.huiyou.dp.service.protocol.UserType}
   *
   * <pre>
   * 用户类型
   * </pre>
   */
  public enum UserType
      implements com.google.protobuf.Internal.EnumLite {
    /**
     * <code>UserType_VIP = 0;</code>
     *
     * <pre>
     *vip用户
     * </pre>
     */
    UserType_VIP(0, 0),
    /**
     * <code>UserType_NOTVIP = 1;</code>
     *
     * <pre>
     *普通用户
     * </pre>
     */
    UserType_NOTVIP(1, 1),
    /**
     * <code>UserType_ADMIN = 2;</code>
     *
     * <pre>
     *管理员用户
     * </pre>
     */
    UserType_ADMIN(2, 2),
    /**
     * <code>UserType_TEMP = 3;</code>
     *
     * <pre>
     *游客
     * </pre>
     */
    UserType_TEMP(3, 3),
    ;

    /**
     * <code>UserType_VIP = 0;</code>
     *
     * <pre>
     *vip用户
     * </pre>
     */
    public static final int UserType_VIP_VALUE = 0;
    /**
     * <code>UserType_NOTVIP = 1;</code>
     *
     * <pre>
     *普通用户
     * </pre>
     */
    public static final int UserType_NOTVIP_VALUE = 1;
    /**
     * <code>UserType_ADMIN = 2;</code>
     *
     * <pre>
     *管理员用户
     * </pre>
     */
    public static final int UserType_ADMIN_VALUE = 2;
    /**
     * <code>UserType_TEMP = 3;</code>
     *
     * <pre>
     *游客
     * </pre>
     */
    public static final int UserType_TEMP_VALUE = 3;


    public final int getNumber() { return value; }

    public static UserType valueOf(int value) {
      switch (value) {
        case 0: return UserType_VIP;
        case 1: return UserType_NOTVIP;
        case 2: return UserType_ADMIN;
        case 3: return UserType_TEMP;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<UserType>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<UserType>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<UserType>() {
            public UserType findValueByNumber(int number) {
              return UserType.valueOf(number);
            }
          };

    private final int value;

    private UserType(int index, int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.huiyou.dp.service.protocol.UserType)
  }

  /**
   * Protobuf enum {@code com.huiyou.dp.service.protocol.UserRegisterStatus}
   *
   * <pre>
   * 注册状态
   * </pre>
   */
  public enum UserRegisterStatus
      implements com.google.protobuf.Internal.EnumLite {
    /**
     * <code>UserStatus_BEING_REGISTERED = 0;</code>
     *
     * <pre>
     *注册中
     * </pre>
     */
    UserStatus_BEING_REGISTERED(0, 0),
    /**
     * <code>UserStatus_DONE = 1;</code>
     *
     * <pre>
     *注册完成
     * </pre>
     */
    UserStatus_DONE(1, 1),
    ;

    /**
     * <code>UserStatus_BEING_REGISTERED = 0;</code>
     *
     * <pre>
     *注册中
     * </pre>
     */
    public static final int UserStatus_BEING_REGISTERED_VALUE = 0;
    /**
     * <code>UserStatus_DONE = 1;</code>
     *
     * <pre>
     *注册完成
     * </pre>
     */
    public static final int UserStatus_DONE_VALUE = 1;


    public final int getNumber() { return value; }

    public static UserRegisterStatus valueOf(int value) {
      switch (value) {
        case 0: return UserStatus_BEING_REGISTERED;
        case 1: return UserStatus_DONE;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<UserRegisterStatus>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<UserRegisterStatus>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<UserRegisterStatus>() {
            public UserRegisterStatus findValueByNumber(int number) {
              return UserRegisterStatus.valueOf(number);
            }
          };

    private final int value;

    private UserRegisterStatus(int index, int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.huiyou.dp.service.protocol.UserRegisterStatus)
  }

  /**
   * Protobuf enum {@code com.huiyou.dp.service.protocol.UserIsLocked}
   *
   * <pre>
   * 锁定状态
   * </pre>
   */
  public enum UserIsLocked
      implements com.google.protobuf.Internal.EnumLite {
    /**
     * <code>UserIsLocked_NOT_LOCKED = 0;</code>
     *
     * <pre>
     *正常状态
     * </pre>
     */
    UserIsLocked_NOT_LOCKED(0, 0),
    /**
     * <code>UserIsLocked_LOCKED = 1;</code>
     *
     * <pre>
     *锁定状态
     * </pre>
     */
    UserIsLocked_LOCKED(1, 1),
    ;

    /**
     * <code>UserIsLocked_NOT_LOCKED = 0;</code>
     *
     * <pre>
     *正常状态
     * </pre>
     */
    public static final int UserIsLocked_NOT_LOCKED_VALUE = 0;
    /**
     * <code>UserIsLocked_LOCKED = 1;</code>
     *
     * <pre>
     *锁定状态
     * </pre>
     */
    public static final int UserIsLocked_LOCKED_VALUE = 1;


    public final int getNumber() { return value; }

    public static UserIsLocked valueOf(int value) {
      switch (value) {
        case 0: return UserIsLocked_NOT_LOCKED;
        case 1: return UserIsLocked_LOCKED;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<UserIsLocked>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<UserIsLocked>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<UserIsLocked>() {
            public UserIsLocked findValueByNumber(int number) {
              return UserIsLocked.valueOf(number);
            }
          };

    private final int value;

    private UserIsLocked(int index, int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.huiyou.dp.service.protocol.UserIsLocked)
  }

  /**
   * Protobuf enum {@code com.huiyou.dp.service.protocol.SmsSendType}
   *
   * <pre>
   * 短信发送类型
   * </pre>
   */
  public enum SmsSendType
      implements com.google.protobuf.Internal.EnumLite {
    /**
     * <code>SmsSendType_REGISTER = 0;</code>
     *
     * <pre>
     *用户注册发送验证码
     * </pre>
     */
    SmsSendType_REGISTER(0, 0),
    /**
     * <code>SmsSendType_FORGET_PASSWORD = 1;</code>
     *
     * <pre>
     *忘记密码发送验证码
     * </pre>
     */
    SmsSendType_FORGET_PASSWORD(1, 1),
    ;

    /**
     * <code>SmsSendType_REGISTER = 0;</code>
     *
     * <pre>
     *用户注册发送验证码
     * </pre>
     */
    public static final int SmsSendType_REGISTER_VALUE = 0;
    /**
     * <code>SmsSendType_FORGET_PASSWORD = 1;</code>
     *
     * <pre>
     *忘记密码发送验证码
     * </pre>
     */
    public static final int SmsSendType_FORGET_PASSWORD_VALUE = 1;


    public final int getNumber() { return value; }

    public static SmsSendType valueOf(int value) {
      switch (value) {
        case 0: return SmsSendType_REGISTER;
        case 1: return SmsSendType_FORGET_PASSWORD;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<SmsSendType>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<SmsSendType>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<SmsSendType>() {
            public SmsSendType findValueByNumber(int number) {
              return SmsSendType.valueOf(number);
            }
          };

    private final int value;

    private SmsSendType(int index, int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.huiyou.dp.service.protocol.SmsSendType)
  }

  /**
   * Protobuf enum {@code com.huiyou.dp.service.protocol.LoginType}
   *
   * <pre>
   *登录类型
   * </pre>
   */
  public enum LoginType
      implements com.google.protobuf.Internal.EnumLite {
    /**
     * <code>LoginType_NORMAL = 0;</code>
     *
     * <pre>
     *普通用户登录
     * </pre>
     */
    LoginType_NORMAL(0, 0),
    /**
     * <code>LoginType_TEMP = 1;</code>
     *
     * <pre>
     *游客登录
     * </pre>
     */
    LoginType_TEMP(1, 1),
    ;

    /**
     * <code>LoginType_NORMAL = 0;</code>
     *
     * <pre>
     *普通用户登录
     * </pre>
     */
    public static final int LoginType_NORMAL_VALUE = 0;
    /**
     * <code>LoginType_TEMP = 1;</code>
     *
     * <pre>
     *游客登录
     * </pre>
     */
    public static final int LoginType_TEMP_VALUE = 1;


    public final int getNumber() { return value; }

    public static LoginType valueOf(int value) {
      switch (value) {
        case 0: return LoginType_NORMAL;
        case 1: return LoginType_TEMP;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<LoginType>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<LoginType>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<LoginType>() {
            public LoginType findValueByNumber(int number) {
              return LoginType.valueOf(number);
            }
          };

    private final int value;

    private LoginType(int index, int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.huiyou.dp.service.protocol.LoginType)
  }

  /**
   * Protobuf enum {@code com.huiyou.dp.service.protocol.DeviceType}
   *
   * <pre>
   *设备类型
   * </pre>
   */
  public enum DeviceType
      implements com.google.protobuf.Internal.EnumLite {
    /**
     * <code>DeviceType_PC = 0;</code>
     *
     * <pre>
     *PC登录
     * </pre>
     */
    DeviceType_PC(0, 0),
    /**
     * <code>DeviceType_ANDROID = 1;</code>
     *
     * <pre>
     *Android登录
     * </pre>
     */
    DeviceType_ANDROID(1, 1),
    /**
     * <code>DeviceType_IOS = 2;</code>
     *
     * <pre>
     *IOS登录
     * </pre>
     */
    DeviceType_IOS(2, 2),
    /**
     * <code>DeviceType_UNKNOWN = 3;</code>
     *
     * <pre>
     *未知设备
     * </pre>
     */
    DeviceType_UNKNOWN(3, 3),
    ;

    /**
     * <code>DeviceType_PC = 0;</code>
     *
     * <pre>
     *PC登录
     * </pre>
     */
    public static final int DeviceType_PC_VALUE = 0;
    /**
     * <code>DeviceType_ANDROID = 1;</code>
     *
     * <pre>
     *Android登录
     * </pre>
     */
    public static final int DeviceType_ANDROID_VALUE = 1;
    /**
     * <code>DeviceType_IOS = 2;</code>
     *
     * <pre>
     *IOS登录
     * </pre>
     */
    public static final int DeviceType_IOS_VALUE = 2;
    /**
     * <code>DeviceType_UNKNOWN = 3;</code>
     *
     * <pre>
     *未知设备
     * </pre>
     */
    public static final int DeviceType_UNKNOWN_VALUE = 3;


    public final int getNumber() { return value; }

    public static DeviceType valueOf(int value) {
      switch (value) {
        case 0: return DeviceType_PC;
        case 1: return DeviceType_ANDROID;
        case 2: return DeviceType_IOS;
        case 3: return DeviceType_UNKNOWN;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<DeviceType>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<DeviceType>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<DeviceType>() {
            public DeviceType findValueByNumber(int number) {
              return DeviceType.valueOf(number);
            }
          };

    private final int value;

    private DeviceType(int index, int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.huiyou.dp.service.protocol.DeviceType)
  }

  /**
   * Protobuf enum {@code com.huiyou.dp.service.protocol.LoginStatus}
   *
   * <pre>
   *登录状态
   * </pre>
   */
  public enum LoginStatus
      implements com.google.protobuf.Internal.EnumLite {
    /**
     * <code>LoginStatus_ERR_OK = 0;</code>
     */
    LoginStatus_ERR_OK(0, 0),
    /**
     * <code>LoginStatus_ERR_USER_NOT_EXSITS = 1;</code>
     */
    LoginStatus_ERR_USER_NOT_EXSITS(1, 1),
    /**
     * <code>LoginStatus_ERR_WRONG_PASSWORD = 2;</code>
     */
    LoginStatus_ERR_WRONG_PASSWORD(2, 2),
    /**
     * <code>LoginStatus_ERR_USER_LOCKED = 3;</code>
     */
    LoginStatus_ERR_USER_LOCKED(3, 3),
    /**
     * <code>LoginStatus_ERR_UNKNOWN = 4;</code>
     */
    LoginStatus_ERR_UNKNOWN(4, 4),
    ;

    /**
     * <code>LoginStatus_ERR_OK = 0;</code>
     */
    public static final int LoginStatus_ERR_OK_VALUE = 0;
    /**
     * <code>LoginStatus_ERR_USER_NOT_EXSITS = 1;</code>
     */
    public static final int LoginStatus_ERR_USER_NOT_EXSITS_VALUE = 1;
    /**
     * <code>LoginStatus_ERR_WRONG_PASSWORD = 2;</code>
     */
    public static final int LoginStatus_ERR_WRONG_PASSWORD_VALUE = 2;
    /**
     * <code>LoginStatus_ERR_USER_LOCKED = 3;</code>
     */
    public static final int LoginStatus_ERR_USER_LOCKED_VALUE = 3;
    /**
     * <code>LoginStatus_ERR_UNKNOWN = 4;</code>
     */
    public static final int LoginStatus_ERR_UNKNOWN_VALUE = 4;


    public final int getNumber() { return value; }

    public static LoginStatus valueOf(int value) {
      switch (value) {
        case 0: return LoginStatus_ERR_OK;
        case 1: return LoginStatus_ERR_USER_NOT_EXSITS;
        case 2: return LoginStatus_ERR_WRONG_PASSWORD;
        case 3: return LoginStatus_ERR_USER_LOCKED;
        case 4: return LoginStatus_ERR_UNKNOWN;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<LoginStatus>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<LoginStatus>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<LoginStatus>() {
            public LoginStatus findValueByNumber(int number) {
              return LoginStatus.valueOf(number);
            }
          };

    private final int value;

    private LoginStatus(int index, int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.huiyou.dp.service.protocol.LoginStatus)
  }

  /**
   * Protobuf enum {@code com.huiyou.dp.service.protocol.BooleanValue}
   *
   * <pre>
   *boolean是否(有时代表失效，未失效，0失效，1未失效)
   * </pre>
   */
  public enum BooleanValue
      implements com.google.protobuf.Internal.EnumLite {
    /**
     * <code>BooleanValue_TRUE = 0;</code>
     */
    BooleanValue_TRUE(0, 0),
    /**
     * <code>BooleanValue_FALSE = 1;</code>
     */
    BooleanValue_FALSE(1, 1),
    ;

    /**
     * <code>BooleanValue_TRUE = 0;</code>
     */
    public static final int BooleanValue_TRUE_VALUE = 0;
    /**
     * <code>BooleanValue_FALSE = 1;</code>
     */
    public static final int BooleanValue_FALSE_VALUE = 1;


    public final int getNumber() { return value; }

    public static BooleanValue valueOf(int value) {
      switch (value) {
        case 0: return BooleanValue_TRUE;
        case 1: return BooleanValue_FALSE;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<BooleanValue>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<BooleanValue>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<BooleanValue>() {
            public BooleanValue findValueByNumber(int number) {
              return BooleanValue.valueOf(number);
            }
          };

    private final int value;

    private BooleanValue(int index, int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.huiyou.dp.service.protocol.BooleanValue)
  }

  /**
   * Protobuf enum {@code com.huiyou.dp.service.protocol.CircleMessageType}
   *
   * <pre>
   *惠友圈消息类型
   * </pre>
   */
  public enum CircleMessageType
      implements com.google.protobuf.Internal.EnumLite {
    /**
     * <code>CircleMessageType_FREEDOM = 0;</code>
     *
     * <pre>
     *自由发布
     * </pre>
     */
    CircleMessageType_FREEDOM(0, 0),
    /**
     * <code>CircleMessageType_FIND_CARD = 1;</code>
     *
     * <pre>
     *求卡
     * </pre>
     */
    CircleMessageType_FIND_CARD(1, 1),
    /**
     * <code>CircleMessageType_FIND_DATE = 2;</code>
     *
     * <pre>
     *求约
     * </pre>
     */
    CircleMessageType_FIND_DATE(2, 2),
    /**
     * <code>CircleMessageType_ALL = 3;</code>
     *
     * <pre>
     *0 1 2类型
     * </pre>
     */
    CircleMessageType_ALL(3, 3),
    ;

    /**
     * <code>CircleMessageType_FREEDOM = 0;</code>
     *
     * <pre>
     *自由发布
     * </pre>
     */
    public static final int CircleMessageType_FREEDOM_VALUE = 0;
    /**
     * <code>CircleMessageType_FIND_CARD = 1;</code>
     *
     * <pre>
     *求卡
     * </pre>
     */
    public static final int CircleMessageType_FIND_CARD_VALUE = 1;
    /**
     * <code>CircleMessageType_FIND_DATE = 2;</code>
     *
     * <pre>
     *求约
     * </pre>
     */
    public static final int CircleMessageType_FIND_DATE_VALUE = 2;
    /**
     * <code>CircleMessageType_ALL = 3;</code>
     *
     * <pre>
     *0 1 2类型
     * </pre>
     */
    public static final int CircleMessageType_ALL_VALUE = 3;


    public final int getNumber() { return value; }

    public static CircleMessageType valueOf(int value) {
      switch (value) {
        case 0: return CircleMessageType_FREEDOM;
        case 1: return CircleMessageType_FIND_CARD;
        case 2: return CircleMessageType_FIND_DATE;
        case 3: return CircleMessageType_ALL;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<CircleMessageType>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<CircleMessageType>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<CircleMessageType>() {
            public CircleMessageType findValueByNumber(int number) {
              return CircleMessageType.valueOf(number);
            }
          };

    private final int value;

    private CircleMessageType(int index, int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.huiyou.dp.service.protocol.CircleMessageType)
  }

  /**
   * Protobuf enum {@code com.huiyou.dp.service.protocol.TehuiType}
   *
   * <pre>
   *特惠类型
   * </pre>
   */
  public enum TehuiType
      implements com.google.protobuf.Internal.EnumLite {
    /**
     * <code>TehuiType_DISCOUNT = 0;</code>
     *
     * <pre>
     *打折
     * </pre>
     */
    TehuiType_DISCOUNT(0, 0),
    /**
     * <code>TehuiType_BACK_CASH = 1;</code>
     *
     * <pre>
     *返现
     * </pre>
     */
    TehuiType_BACK_CASH(1, 1),
    /**
     * <code>TehuiType_BACK_TICKET = 2;</code>
     *
     * <pre>
     *返券
     * </pre>
     */
    TehuiType_BACK_TICKET(2, 2),
    /**
     * <code>TehuiType_FULL_CUT = 3;</code>
     *
     * <pre>
     *满减
     * </pre>
     */
    TehuiType_FULL_CUT(3, 3),
    /**
     * <code>TehuiType_GIVE_SCORE = 4;</code>
     *
     * <pre>
     *赠送积分
     * </pre>
     */
    TehuiType_GIVE_SCORE(4, 4),
    /**
     * <code>TehuiType_UNKNOWN = 5;</code>
     *
     * <pre>
     *未知类型
     * </pre>
     */
    TehuiType_UNKNOWN(5, 5),
    ;

    /**
     * <code>TehuiType_DISCOUNT = 0;</code>
     *
     * <pre>
     *打折
     * </pre>
     */
    public static final int TehuiType_DISCOUNT_VALUE = 0;
    /**
     * <code>TehuiType_BACK_CASH = 1;</code>
     *
     * <pre>
     *返现
     * </pre>
     */
    public static final int TehuiType_BACK_CASH_VALUE = 1;
    /**
     * <code>TehuiType_BACK_TICKET = 2;</code>
     *
     * <pre>
     *返券
     * </pre>
     */
    public static final int TehuiType_BACK_TICKET_VALUE = 2;
    /**
     * <code>TehuiType_FULL_CUT = 3;</code>
     *
     * <pre>
     *满减
     * </pre>
     */
    public static final int TehuiType_FULL_CUT_VALUE = 3;
    /**
     * <code>TehuiType_GIVE_SCORE = 4;</code>
     *
     * <pre>
     *赠送积分
     * </pre>
     */
    public static final int TehuiType_GIVE_SCORE_VALUE = 4;
    /**
     * <code>TehuiType_UNKNOWN = 5;</code>
     *
     * <pre>
     *未知类型
     * </pre>
     */
    public static final int TehuiType_UNKNOWN_VALUE = 5;


    public final int getNumber() { return value; }

    public static TehuiType valueOf(int value) {
      switch (value) {
        case 0: return TehuiType_DISCOUNT;
        case 1: return TehuiType_BACK_CASH;
        case 2: return TehuiType_BACK_TICKET;
        case 3: return TehuiType_FULL_CUT;
        case 4: return TehuiType_GIVE_SCORE;
        case 5: return TehuiType_UNKNOWN;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<TehuiType>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<TehuiType>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<TehuiType>() {
            public TehuiType findValueByNumber(int number) {
              return TehuiType.valueOf(number);
            }
          };

    private final int value;

    private TehuiType(int index, int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.huiyou.dp.service.protocol.TehuiType)
  }

  /**
   * Protobuf enum {@code com.huiyou.dp.service.protocol.FavoriteTimeInterval}
   *
   * <pre>
   *时间区间
   * </pre>
   */
  public enum FavoriteTimeInterval
      implements com.google.protobuf.Internal.EnumLite {
    /**
     * <code>FavoriteTimeInterval_ZERO_TO_ONE_MONTH = 0;</code>
     *
     * <pre>
     *1个月之内
     * </pre>
     */
    FavoriteTimeInterval_ZERO_TO_ONE_MONTH(0, 0),
    /**
     * <code>FavoriteTimeInterval_ONE_TO_THREE_MONTH = 1;</code>
     *
     * <pre>
     *1到3个月
     * </pre>
     */
    FavoriteTimeInterval_ONE_TO_THREE_MONTH(1, 1),
    /**
     * <code>FavoriteTimeInterval_OVERFLOW_THREE_MONTH = 2;</code>
     *
     * <pre>
     *超过3个月
     * </pre>
     */
    FavoriteTimeInterval_OVERFLOW_THREE_MONTH(2, 2),
    /**
     * <code>FavoriteTimeInterval_ALL_INTERCAL = 3;</code>
     *
     * <pre>
     *全部
     * </pre>
     */
    FavoriteTimeInterval_ALL_INTERCAL(3, 3),
    ;

    /**
     * <code>FavoriteTimeInterval_ZERO_TO_ONE_MONTH = 0;</code>
     *
     * <pre>
     *1个月之内
     * </pre>
     */
    public static final int FavoriteTimeInterval_ZERO_TO_ONE_MONTH_VALUE = 0;
    /**
     * <code>FavoriteTimeInterval_ONE_TO_THREE_MONTH = 1;</code>
     *
     * <pre>
     *1到3个月
     * </pre>
     */
    public static final int FavoriteTimeInterval_ONE_TO_THREE_MONTH_VALUE = 1;
    /**
     * <code>FavoriteTimeInterval_OVERFLOW_THREE_MONTH = 2;</code>
     *
     * <pre>
     *超过3个月
     * </pre>
     */
    public static final int FavoriteTimeInterval_OVERFLOW_THREE_MONTH_VALUE = 2;
    /**
     * <code>FavoriteTimeInterval_ALL_INTERCAL = 3;</code>
     *
     * <pre>
     *全部
     * </pre>
     */
    public static final int FavoriteTimeInterval_ALL_INTERCAL_VALUE = 3;


    public final int getNumber() { return value; }

    public static FavoriteTimeInterval valueOf(int value) {
      switch (value) {
        case 0: return FavoriteTimeInterval_ZERO_TO_ONE_MONTH;
        case 1: return FavoriteTimeInterval_ONE_TO_THREE_MONTH;
        case 2: return FavoriteTimeInterval_OVERFLOW_THREE_MONTH;
        case 3: return FavoriteTimeInterval_ALL_INTERCAL;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<FavoriteTimeInterval>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<FavoriteTimeInterval>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<FavoriteTimeInterval>() {
            public FavoriteTimeInterval findValueByNumber(int number) {
              return FavoriteTimeInterval.valueOf(number);
            }
          };

    private final int value;

    private FavoriteTimeInterval(int index, int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.huiyou.dp.service.protocol.FavoriteTimeInterval)
  }

  /**
   * Protobuf enum {@code com.huiyou.dp.service.protocol.UserSex}
   *
   * <pre>
   *用户性别
   * </pre>
   */
  public enum UserSex
      implements com.google.protobuf.Internal.EnumLite {
    /**
     * <code>UserSex_MALE = 0;</code>
     *
     * <pre>
     *男性
     * </pre>
     */
    UserSex_MALE(0, 0),
    /**
     * <code>UserSex_FEMALE = 1;</code>
     *
     * <pre>
     *女性
     * </pre>
     */
    UserSex_FEMALE(1, 1),
    ;

    /**
     * <code>UserSex_MALE = 0;</code>
     *
     * <pre>
     *男性
     * </pre>
     */
    public static final int UserSex_MALE_VALUE = 0;
    /**
     * <code>UserSex_FEMALE = 1;</code>
     *
     * <pre>
     *女性
     * </pre>
     */
    public static final int UserSex_FEMALE_VALUE = 1;


    public final int getNumber() { return value; }

    public static UserSex valueOf(int value) {
      switch (value) {
        case 0: return UserSex_MALE;
        case 1: return UserSex_FEMALE;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<UserSex>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<UserSex>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<UserSex>() {
            public UserSex findValueByNumber(int number) {
              return UserSex.valueOf(number);
            }
          };

    private final int value;

    private UserSex(int index, int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.huiyou.dp.service.protocol.UserSex)
  }

  /**
   * Protobuf enum {@code com.huiyou.dp.service.protocol.PassStatus}
   *
   * <pre>
   *审核状态
   * </pre>
   */
  public enum PassStatus
      implements com.google.protobuf.Internal.EnumLite {
    /**
     * <code>PassStatus_NOT_PASS = 0;</code>
     *
     * <pre>
     *审核不通过
     * </pre>
     */
    PassStatus_NOT_PASS(0, 0),
    /**
     * <code>PassStatus_PASS = 1;</code>
     *
     * <pre>
     *审核通过
     * </pre>
     */
    PassStatus_PASS(1, 1),
    /**
     * <code>PassStatus_PASSING = 2;</code>
     *
     * <pre>
     *审核中
     * </pre>
     */
    PassStatus_PASSING(2, 2),
    ;

    /**
     * <code>PassStatus_NOT_PASS = 0;</code>
     *
     * <pre>
     *审核不通过
     * </pre>
     */
    public static final int PassStatus_NOT_PASS_VALUE = 0;
    /**
     * <code>PassStatus_PASS = 1;</code>
     *
     * <pre>
     *审核通过
     * </pre>
     */
    public static final int PassStatus_PASS_VALUE = 1;
    /**
     * <code>PassStatus_PASSING = 2;</code>
     *
     * <pre>
     *审核中
     * </pre>
     */
    public static final int PassStatus_PASSING_VALUE = 2;


    public final int getNumber() { return value; }

    public static PassStatus valueOf(int value) {
      switch (value) {
        case 0: return PassStatus_NOT_PASS;
        case 1: return PassStatus_PASS;
        case 2: return PassStatus_PASSING;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<PassStatus>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<PassStatus>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<PassStatus>() {
            public PassStatus findValueByNumber(int number) {
              return PassStatus.valueOf(number);
            }
          };

    private final int value;

    private PassStatus(int index, int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.huiyou.dp.service.protocol.PassStatus)
  }

  /**
   * Protobuf enum {@code com.huiyou.dp.service.protocol.CardType}
   *
   * <pre>
   *卡包卡片类型
   * </pre>
   */
  public enum CardType
      implements com.google.protobuf.Internal.EnumLite {
    /**
     * <code>CardType_COUPON = 0;</code>
     *
     * <pre>
     *商家优惠券
     * </pre>
     */
    CardType_COUPON(0, 0),
    ;

    /**
     * <code>CardType_COUPON = 0;</code>
     *
     * <pre>
     *商家优惠券
     * </pre>
     */
    public static final int CardType_COUPON_VALUE = 0;


    public final int getNumber() { return value; }

    public static CardType valueOf(int value) {
      switch (value) {
        case 0: return CardType_COUPON;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<CardType>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<CardType>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<CardType>() {
            public CardType findValueByNumber(int number) {
              return CardType.valueOf(number);
            }
          };

    private final int value;

    private CardType(int index, int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.huiyou.dp.service.protocol.CardType)
  }

  /**
   * Protobuf enum {@code com.huiyou.dp.service.protocol.IdentifyMerchantPassStatus}
   *
   * <pre>
   *商户认领审核状态
   * </pre>
   */
  public enum IdentifyMerchantPassStatus
      implements com.google.protobuf.Internal.EnumLite {
    /**
     * <code>IdentifyMerchantPassStatus_RESOURCE_NOT_UPLOAD = 0;</code>
     *
     * <pre>
     *资料未上传
     * </pre>
     */
    IdentifyMerchantPassStatus_RESOURCE_NOT_UPLOAD(0, 0),
    /**
     * <code>IdentifyMerchantPassStatus_PASSING = 1;</code>
     *
     * <pre>
     *资料已上传，审核中
     * </pre>
     */
    IdentifyMerchantPassStatus_PASSING(1, 1),
    /**
     * <code>IdentifyMerchantPassStatus_NOT_PASS = 2;</code>
     *
     * <pre>
     *审核不通过
     * </pre>
     */
    IdentifyMerchantPassStatus_NOT_PASS(2, 2),
    /**
     * <code>IdentifyMerchantPassStatus_PASS = 3;</code>
     *
     * <pre>
     *审核通过
     * </pre>
     */
    IdentifyMerchantPassStatus_PASS(3, 3),
    ;

    /**
     * <code>IdentifyMerchantPassStatus_RESOURCE_NOT_UPLOAD = 0;</code>
     *
     * <pre>
     *资料未上传
     * </pre>
     */
    public static final int IdentifyMerchantPassStatus_RESOURCE_NOT_UPLOAD_VALUE = 0;
    /**
     * <code>IdentifyMerchantPassStatus_PASSING = 1;</code>
     *
     * <pre>
     *资料已上传，审核中
     * </pre>
     */
    public static final int IdentifyMerchantPassStatus_PASSING_VALUE = 1;
    /**
     * <code>IdentifyMerchantPassStatus_NOT_PASS = 2;</code>
     *
     * <pre>
     *审核不通过
     * </pre>
     */
    public static final int IdentifyMerchantPassStatus_NOT_PASS_VALUE = 2;
    /**
     * <code>IdentifyMerchantPassStatus_PASS = 3;</code>
     *
     * <pre>
     *审核通过
     * </pre>
     */
    public static final int IdentifyMerchantPassStatus_PASS_VALUE = 3;


    public final int getNumber() { return value; }

    public static IdentifyMerchantPassStatus valueOf(int value) {
      switch (value) {
        case 0: return IdentifyMerchantPassStatus_RESOURCE_NOT_UPLOAD;
        case 1: return IdentifyMerchantPassStatus_PASSING;
        case 2: return IdentifyMerchantPassStatus_NOT_PASS;
        case 3: return IdentifyMerchantPassStatus_PASS;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<IdentifyMerchantPassStatus>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<IdentifyMerchantPassStatus>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<IdentifyMerchantPassStatus>() {
            public IdentifyMerchantPassStatus findValueByNumber(int number) {
              return IdentifyMerchantPassStatus.valueOf(number);
            }
          };

    private final int value;

    private IdentifyMerchantPassStatus(int index, int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.huiyou.dp.service.protocol.IdentifyMerchantPassStatus)
  }

  /**
   * Protobuf enum {@code com.huiyou.dp.service.protocol.EffectivePeriodType}
   *
   * <pre>
   *生效周期类型
   * </pre>
   */
  public enum EffectivePeriodType
      implements com.google.protobuf.Internal.EnumLite {
    /**
     * <code>EffectivePeriodType_WEEK = 0;</code>
     *
     * <pre>
     *按周
     * </pre>
     */
    EffectivePeriodType_WEEK(0, 0),
    /**
     * <code>EffectivePeriodType_MONTH = 1;</code>
     *
     * <pre>
     *按月
     * </pre>
     */
    EffectivePeriodType_MONTH(1, 1),
    ;

    /**
     * <code>EffectivePeriodType_WEEK = 0;</code>
     *
     * <pre>
     *按周
     * </pre>
     */
    public static final int EffectivePeriodType_WEEK_VALUE = 0;
    /**
     * <code>EffectivePeriodType_MONTH = 1;</code>
     *
     * <pre>
     *按月
     * </pre>
     */
    public static final int EffectivePeriodType_MONTH_VALUE = 1;


    public final int getNumber() { return value; }

    public static EffectivePeriodType valueOf(int value) {
      switch (value) {
        case 0: return EffectivePeriodType_WEEK;
        case 1: return EffectivePeriodType_MONTH;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<EffectivePeriodType>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<EffectivePeriodType>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<EffectivePeriodType>() {
            public EffectivePeriodType findValueByNumber(int number) {
              return EffectivePeriodType.valueOf(number);
            }
          };

    private final int value;

    private EffectivePeriodType(int index, int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.huiyou.dp.service.protocol.EffectivePeriodType)
  }


  static {
  }

  // @@protoc_insertion_point(outer_class_scope)
}
