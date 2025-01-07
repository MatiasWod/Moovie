import SearchableMediaTag from "../searchableMediaTag/searchableMediaTag";

const MediaTag = ({ link, image=null , text, id, style, darkmode=false }) => (
    <sup
        className={`badge ${darkmode ? "text-bg-dark" : "text-bg-light border border-black"} `}
        style={{ marginLeft: '10px', ...style }}
    >
        {link ? (
            <SearchableMediaTag link={link} text={text} image={image} id={id}/>
        ) : (
            <span>{text}</span>
        )}
    </sup>
);


export default MediaTag;