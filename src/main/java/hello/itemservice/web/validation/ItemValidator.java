package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
        // Item-= clazz 즉 자기 자신이냐
        // item에 자식이어도 통과되게!
    }
    @Override
    public void validate(Object target, Errors errors) {
        // Errors가 bindingResult의 부모임!
        Item item = (Item)target;
        if(!StringUtils.hasText(item.getItemName())) {
            // itemName에 글자가 없을시
            // bindingResult.addError(new FieldError("item", "itemName",item.getItemName(), false, new String[]{"required.item.itemName"},null, "상품이름은 필수입니다!"));
            errors.rejectValue("itemName","required"); // required + 오브젝트 이름 + 필드 이름!
        }
        if(item.getPrice()==null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            //bindingResult.addError(new FieldError("item","price",item.getPrice(),false,new String[]{"range.item.price"},new Object[]{1000,1000000},"가격은 1000원과 백만원 사이!"));
            errors.rejectValue("price", "range", new Object[]{1000,100000}, null);
        }
        if(item.getQuantity() == null || item.getQuantity() >= 9999) {
            //bindingResult.addError(new FieldError("item","quantity", item.getQuantity(),false,new String[]{"max.item.quantity"},new Object[]{9999}, null ));
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }
        // 특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null  ){
            int result =  item.getPrice() * item.getQuantity();
            if(result < 10000) {
                //bindingResult.addError(new ObjectError("item",new String[]{"totalPriceMin"},new Object[]{10000, result},null));
                errors.reject("totalPriceMin", new Object[]{10000,result }, null);
            }
        }

    }
}
