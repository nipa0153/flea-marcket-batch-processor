package com.example.NotUsed.Processor;
// package com.example.ChunkBatch.Processor;

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Map;
// import java.util.Set;

// import org.springframework.batch.item.ItemProcessor;

// import com.example.DTO.CategoryDto;
// import com.example.Domain.Category;

// public class CategoryItemProcessor implements ItemProcessor<CategoryDto,
// Category> {

// @Override
// public Category process(CategoryDto categoryDto) {

// String parent = categoryDto.getParent();
// String child = categoryDto.getChild();
// String grandChild = categoryDto.getGrandChild();

// // parentの重複を省くためのSet
// Set<String> distinctNames = new HashSet<>();

// // child,grandchildの重複を省くためのMap
// Map<String, Boolean> distinctMap = new HashMap<>();
// String combinationChildKey = parent + "|" + child;
// String combinationGrandChildKey = child + "|" + grandChild;

// /**
// * addメソッドで要素が追加された場合にtrueを返す
// */
// if (distinctNames.add(parent)) {
// Category parenCategory = new Category();
// parenCategory.setName(parent);
// }
// if (!distinctMap.containsKey(combinationChildKey)) {
// Category childCategory = new Category();
// childCategory.setName(child);
// childCategory.setParentId();
// }
// if (!distinctMap.containsKey(combinationGrandChildKey)) {

// }
// }
// }
