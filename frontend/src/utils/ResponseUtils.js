import { parseLinkHeader } from '@web3-storage/parse-link-header';

export const parsePaginatedResponse = (response) => {
  if (!response || !response.data) {
    return { links: null, data: null };
  }
  const linkHeader = response.headers?.link;
  const links = linkHeader ? parseLinkHeader(linkHeader) : null;
  const totalElements =
    Number(response.headers?.['total-elements']) ||
    Number(response.headers?.['Total-Elements']) ||
    Number(response.data.length) ||
    0;
  const data = response.data;
  const status = response.status;
  return { links, data, status, totalElements };
};
