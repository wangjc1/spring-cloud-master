import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.StringUtils;
import org.springframework.validation.DataBinder;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class DataBinderTest {

	private final DefaultConversionService conversionService = new DefaultConversionService();

	@Before
	public void setUp() {
		conversionService.addConverter(new StringToPhoneNumberConverter());
	}

	/**
	 *
	 DataBinder主要提供了两个功能：
		 利用MutablePropertyValues，给对象的属性设值
		 在设值的同时做Validation
	 */
	@Test
	public void testDataBind() {
		DataHolder bean = new DataHolder();
		DataBinder binder = new DataBinder(bean);
		binder.setConversionService(conversionService);

		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("phoneNumber", "010-12345678");
		binder.bind(propertyValues);

		assertEquals(0, binder.getBindingResult().getErrorCount());
		assertEquals(bean.getPhoneNumber().areaCode, "010");
	}


	/**
	 * BeanWrapper是一个方便开发人员使用字符串来对Java Bean的属性执行get、set操作的工具类
	 * 和DataBinder一样，BeanWrapper在属性设置的过程中，也可以提供转换格式化服务
	 */
	@Test
	public void testBeanWrapper() {
		DataHolder bean = new DataHolder();

		BeanWrapperImpl beanWrapper = new BeanWrapperImpl(bean);
		beanWrapper.setConversionService(conversionService);
		beanWrapper.setPropertyValue("phoneNumber", "010-12345678");

		assertEquals(bean.getPhoneNumber().areaCode, "010");
	}

	/**
	 *  利用反射对BeanWrapper属性进行设置
	 */
	@Test
	public void testBeanWrapper2() {
		Company c = new Company();
		BeanWrapper bwComp = new BeanWrapperImpl(c);
		// setting the company name...
		bwComp.setPropertyValue("name", "Some Company Inc.");
		// ... can also be done like this:
		PropertyValue v = new PropertyValue("name", "Some Company Inc.");
		bwComp.setPropertyValue(v);

		// ok, lets create the director and tie it to the company:
		Employee jim = new Employee();
		BeanWrapper bwJim = new BeanWrapperImpl(jim);
		bwJim.setPropertyValue("salary", 100.2);

		//set the property 'managingDirector' of Company with jim
		bwComp.setPropertyValue("managingDirector", jim);

		// retrieving the salary of the managingDirector through the company
		Float salary = (Float)bwComp.getPropertyValue("managingDirector.salary");

		assertEquals(salary, new Float(jim.getSalary()));
	}


	public static class DataHolder {

		private PhoneNumberModel phoneNumber;

		public PhoneNumberModel getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(PhoneNumberModel phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
	}

	public static class StringToPhoneNumberConverter implements Converter<String, PhoneNumberModel> {

		Pattern pattern = Pattern.compile("^(\\d{3,4})-(\\d{7,8})$");

		@Override
		public PhoneNumberModel convert(String source) {

			if(!StringUtils.hasLength(source)) {
				//①如果source为空 返回null
				return null;
			}

			Matcher matcher = pattern.matcher(source);
			if(matcher.matches()) {
				//②如果匹配 进行转换
				PhoneNumberModel phoneNumber = new PhoneNumberModel();
				phoneNumber.setAreaCode(matcher.group(1));
				phoneNumber.setPhoneNumber(matcher.group(2));
				return phoneNumber;
			} else {
				//③如果不匹配 转换失败
				throw new IllegalArgumentException(String.format("类型转换失败，需要格式[010-12345678]，但格式是[%s]", source));
			}
		}

	}

	//如格式010-12345678
	public static class PhoneNumberModel {
		private String areaCode;//区号
		private String phoneNumber;//电话号码

		public PhoneNumberModel() {
		}

		public PhoneNumberModel(String areaCode, String phoneNumber) {
			this.areaCode = areaCode;
			this.phoneNumber = phoneNumber;
		}

		public String getAreaCode() {
			return areaCode;
		}
		public void setAreaCode(String areaCode) {
			this.areaCode = areaCode;
		}
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((areaCode == null) ? 0 : areaCode.hashCode());
			result = prime * result
					+ ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PhoneNumberModel other = (PhoneNumberModel) obj;
			if (areaCode == null) {
				if (other.areaCode != null)
					return false;
			} else if (!areaCode.equals(other.areaCode))
				return false;
			if (phoneNumber == null) {
				if (other.phoneNumber != null)
					return false;
			} else if (!phoneNumber.equals(other.phoneNumber))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "PhoneNumberModel [areaCode=" + areaCode + ", phoneNumber="
					+ phoneNumber + "]";
		}
	}

	public static class Company {
		private String name;
		private Employee managingDirector;
		private Date date;

		public String getName() {
			return this.name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Employee getManagingDirector() {
			return this.managingDirector;
		}
		public void setManagingDirector(Employee managingDirector) {
			this.managingDirector = managingDirector;
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}

	}

	public static class Employee {
		private float salary;
		public float getSalary() {
			return salary;
		}
		public void setSalary(float salary) {
			this.salary = salary;
		}
	}


}