import SearchableMediaTag from "../searchableMediaTag/searchableMediaTag";

const MediaTag = ({ link, text, style }) => (
    <sup className="badge text-bg-light border border-black" style={{ marginLeft:'10px', ...style}}>
        {link ? (
            <SearchableMediaTag link={link} text={text} />
        ) : (
        <span>{text}</span>
        )}
    </sup>
);

export default MediaTag;