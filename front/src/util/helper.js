export const cutLongString = (text, count_lit) => {
  var all_len = text.length;
  if (all_len > count_lit)
    text = text.substr(0, (count_lit - 3)) + '...';
  return text;
}
